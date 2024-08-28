package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.TutorApplication;
import com.tutorconnect.app.model.Student;
import com.tutorconnect.app.model.Tutor;

import java.util.UUID;
import java.util.regex.Pattern;

public class AddStudent extends AppCompatActivity {

    private static final String defaultStudentPassword = "password";

    EditText et_email, et_name, et_subject;
    Button btn_Register;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    DatabaseReference dbInstance;

    Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        setTitle("Add Student");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        et_name = findViewById(R.id.parent_name);
        et_email = findViewById(R.id.et_email);
        et_subject = findViewById(R.id.et_subject);
        btn_Register = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);

        tutor = ((TutorApplication) getApplicationContext()).tutor;

        dbInstance = FirebaseDatabase.getInstance().getReference();

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });
    }

    private void validateEmailExistenceAndAddStudent(String name, String email, String subject) {
        progressDialog.setMessage("Adding Student....");
        progressDialog.setTitle("Adding");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Query emailQuery = dbInstance.child("students").orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(AddStudent.this, "Student with the same email already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    // create new record for user in firebase-realtime-database
                    String id = UUID.randomUUID().toString();
                    Student tutorc = new Student(name, subject, email, defaultStudentPassword, id, tutor.getId() , true, null);
                    dbInstance.child("students").child(id).setValue(tutorc).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(AddStudent.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                                sendUserToMainActivity();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddStudent.this, "Error adding student, try again later..!", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Registration failed", task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddStudent.this, "Error adding student, try again later..!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.e("Firebase", "Database error", databaseError.toException());
                Toast.makeText(AddStudent.this, "Error Adding student, try again later..!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PerformAuth() {
        String email = et_email.getText().toString();
        String name = et_name.getText().toString();
        String subject = et_subject.getText().toString();

        if (name.isEmpty()) {
            et_subject.setError("Please Enter Name");
            return;
        } else if (email.isEmpty()) {
            et_email.setError("Please Enter Email");
            return;
        } else if (!pat.matcher(email).matches()) {
            et_email.setError("Please Enter a valid Email");
            return;
        } else if (subject.isEmpty()) {
            et_subject.setError("Please Enter Subject");
            return;
        } else {
            validateEmailExistenceAndAddStudent(name, email, subject);
        }
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(AddStudent.this, TutorViewStudents.class);
        intent.putExtra("email", tutor.getEmail());
        intent.putExtra("password", tutor.getPassword());
        intent.putExtra("tutorId", tutor.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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