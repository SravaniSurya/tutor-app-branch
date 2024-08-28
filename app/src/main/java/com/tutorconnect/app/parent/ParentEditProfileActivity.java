package com.tutorconnect.app.parent;

import android.app.ProgressDialog;
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

import java.util.Objects;
import java.util.regex.Pattern;

public class ParentEditProfileActivity extends AppCompatActivity {

    EditText et_Email;
    EditText et_password;

    Button btn_update;

    Parent parent;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_edit_profile);
        setTitle("Update profile");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        et_Email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_update = findViewById(R.id.btn_update);

        parent = ((TutorApplication) getApplicationContext()).parent;

        et_Email.setText(parent.getEmail());
        et_password.setText(parent.getPassword());

        progressDialog = new ProgressDialog(this);

        dbReference = FirebaseDatabase.getInstance().getReference();

        btn_update.setOnClickListener(v -> PerformAuth());
    }

    private void PerformAuth() {
        String email = et_Email.getText().toString();
        String password = et_password.getText().toString();

        if (email.isEmpty()) {
            et_Email.setError("Please Enter Email");
        } else if (!pat.matcher(email).matches()) {
            et_Email.setError("Please Enter a valid Email");
        } else if (password.isEmpty()) {
            et_password.setError("Please input Password");
        } else if (password.length() < 6) {
            et_password.setError("Password too short");
        } else {
            verifyEmailExistenceAndCreateTutorAccount(email, password);
        }
    }

    private void verifyEmailExistenceAndCreateTutorAccount(String email, String password) {
        progressDialog.setMessage("Updating....");
        progressDialog.setTitle("Updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Query emailQuery = dbReference.child("parents").orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !Objects.equals(email, parent.getEmail())) {
                    et_Email.setError("Email already exists");
                    progressDialog.dismiss();
                    Toast.makeText(ParentEditProfileActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    parent.setEmail(email);
                    parent.setPassword(password);
                    // create new record for user in firebase-realtime-database
                    dbReference.child("parents").child(parent.getId()).setValue(parent).addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(ParentEditProfileActivity.this, "Updated..!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ParentEditProfileActivity.this, "Error updating user, try again later..!", Toast.LENGTH_SHORT).show();
                            Log.e("Firebase", "Update failed", task.getException());
                        }
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ParentEditProfileActivity.this, "Error updating user, try again later..!",
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.e("Firebase", "Database error", databaseError.toException());
                Toast.makeText(ParentEditProfileActivity.this, "Error updating user, try again later..!",
                        Toast.LENGTH_SHORT).show();
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