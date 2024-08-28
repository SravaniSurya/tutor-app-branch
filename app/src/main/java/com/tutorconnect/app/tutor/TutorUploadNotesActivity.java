package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tutorconnect.app.R;
import com.tutorconnect.app.TutorApplication;
import com.tutorconnect.app.model.AssignmentNotes;
import com.tutorconnect.app.model.Tutor;

import java.util.UUID;

public class TutorUploadNotesActivity extends AppCompatActivity {
    EditText editText;
    Button btnUploadPdf, btn_selectPdf;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_upload_notes);

        setTitle("Tutor - Add notes");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editText = findViewById(R.id.editext);
        btnUploadPdf = findViewById(R.id.btn);
        btn_selectPdf = findViewById(R.id.btn_selectPdf);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnUploadPdf.setEnabled(false);
        btn_selectPdf.setOnClickListener(v -> selectPdf());

        tutor = ((TutorApplication) getApplicationContext()).tutor;
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT "), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            btnUploadPdf.setEnabled(true);
            btnUploadPdf.setOnClickListener(v -> {
                String name = editText.getText().toString();
                if (name.isEmpty()) {
                    editText.setError("Empty name");
                    return;
                }
                uploadFileToFirebase(data.getData(), name);
            });
        }
    }

    private void uploadFileToFirebase(Uri data, String name) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();
        StorageReference reference = storageReference.child("upload" + System.currentTimeMillis() + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri uri = uriTask.getResult();


                    String id = UUID.randomUUID().toString();
                    AssignmentNotes assignmentNotes = new AssignmentNotes(id, name, uri.toString(), tutor.getId());

                    databaseReference.child("notes").child(id).setValue(assignmentNotes);

                    Toast.makeText(TutorUploadNotesActivity.this, "Notes Uploaded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    editText.setText("");

                    btnUploadPdf.setEnabled(false);
                })
                .addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage("File Uploaded.." + (int) progress + "%");
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Custom logs", "onFailure: ", e);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error uploading file", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Handle the back button action
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}