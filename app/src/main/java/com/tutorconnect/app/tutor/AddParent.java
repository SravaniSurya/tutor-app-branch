package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.TutorApplication;
import com.tutorconnect.app.model.Parent;
import com.tutorconnect.app.model.Tutor;

import java.util.UUID;
import java.util.regex.Pattern;

public class AddParent extends AppCompatActivity {

    private static final String defaultParentPassword = "password";

    EditText et_email, et_name;
    Button btn_Register;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    DatabaseReference dbReference;

    Intent intent;
    String studentId = "";
    String studentName = "";

    Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent);

        intent = getIntent();
        studentId = intent.getStringExtra("studentId");
        studentName = intent.getStringExtra("studentName");

        setTitle("Add parent - " + studentName);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tutor = ((TutorApplication) getApplicationContext()).tutor;

        et_name = findViewById(R.id.parent_name);
        et_email = findViewById(R.id.et_email);
        btn_Register = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);

        dbReference = FirebaseDatabase.getInstance().getReference();

        btn_Register.setOnClickListener(v -> PerformAuth());
    }

    private void PerformAuth() {
        String email = et_email.getText().toString();
        String name = et_name.getText().toString();

        if (name.isEmpty()) {
            et_email.setError("Please Enter Name");
        } else if (email.isEmpty()) {
            et_email.setError("Please Enter Email");
        } else if (!pat.matcher(email).matches()) {
            et_email.setError("Please Enter a valid Email");
        } else {
            verifyEmailExistenceAndAddParent(name, email, tutor.getId(), studentId);
        }
    }

    private void verifyEmailExistenceAndAddParent(String name, String email,
                                                  String tutorId, String studentId) {
        progressDialog.setMessage("Adding parent....");
        progressDialog.setTitle("Adding");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String compositeKey = tutorId + "_" + studentId;

        Query emailQuery = dbReference.child("parents").orderByChild("email")
                .equalTo(email);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean compositeKeyFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Parent parent = snapshot.getValue(Parent.class);
                    if (parent != null && compositeKey.equals(parent.getCompositeKey())) {
                        compositeKeyFound = true;
                        break;
                    }
                }

                progressDialog.dismiss();
                if (compositeKeyFound) {
                    Toast.makeText(AddParent.this, "Parent with the same email exists.", Toast.LENGTH_SHORT).show();
                } else {
                    addNewParent(name, email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.e("Firebase", "Database error", databaseError.toException());
                Toast.makeText(AddParent.this, "Error adding parent, try again later..!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewParent(String name, String email) {
        // create new record for user in firebase-realtime-database
        String id = UUID.randomUUID().toString();
        String compositeKey = tutor.getId() + "_" + studentId;
        Parent tutorC = new Parent(name, email, defaultParentPassword, id, studentId, tutor.getId(), compositeKey, true);
        dbReference.child("parents").child(id).setValue(tutorC)
                .addOnCompleteListener(task -> {
                    // update the parent id for child object.
                    dbReference.child("students").child(studentId).child("parentId").setValue(id)
                            .addOnCompleteListener(task1 -> {
                                progressDialog.dismiss();
                                if (task1.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddParent.this, "Parent added successfully", Toast.LENGTH_SHORT).show();
                                    sendUserToMainActivity();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddParent.this, "Error adding parent, try again later..!", Toast.LENGTH_SHORT).show();
                                    Log.e("Firebase", "Registration failed", task.getException());
                                }
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(AddParent.this, "Error adding parent, try again later..!",
                                        Toast.LENGTH_SHORT).show();
                            });
                });
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(AddParent.this, TutorViewParent.class);
        intent.putExtra("tutorId", tutor.getId());
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        startActivity(intent);
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