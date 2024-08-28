package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.tutorconnect.app.model.Tutor;

import java.util.regex.Pattern;

public class TutorSignIn extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login;
    TextView tv_registerBtn;

    TextView tv_forgotPassword;

    DatabaseReference dbInstance;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_signin);

        setTitle("Tutor");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_registerBtn = findViewById(R.id.tv_registerButton);
        tv_forgotPassword = findViewById(R.id.tv_forgotPassword);

        tv_forgotPassword.setOnClickListener(v -> startActivity(new Intent(TutorSignIn.this, TutorResetPasswordActivity.class)));

        progressDialog = new ProgressDialog(this);

        dbInstance = FirebaseDatabase.getInstance().getReference();

        btn_login.setOnClickListener(v -> performLogin());

        tv_registerBtn.setOnClickListener(v -> startActivity(new Intent(TutorSignIn.this, TutorSignUp.class)));

    }

    private void performLogin() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (email.isEmpty()) {
            et_email.setError("Please Enter Email");
        } else if (!pat.matcher(email).matches()) {
            et_email.setError("Please Enter a valid Email");
        } else if (password.isEmpty()) {
            et_password.setError("Please input Password");
        } else if (password.length() < 6) {
            et_password.setError("Password too short");
        } else {
            progressDialog.setMessage("Validating credentials....");
            progressDialog.setTitle("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // validate credentials with email and password
            Query emailQuery = dbInstance.child("tutors").orderByChild("email").equalTo(email);
            emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()) {
                            Tutor tutor = tutorSnapshot.getValue(Tutor.class);
                            if (tutor != null && password.equals(tutor.getPassword())) {
                                // check if password is valid
                                Toast.makeText(TutorSignIn.this, "SignIn success.", Toast.LENGTH_SHORT).show();
                                sendUserToMainActivity(tutor);
                                return; // Exit once a match is found
                            }
                        }

                        Toast.makeText(TutorSignIn.this, "Invalid password..!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TutorSignIn.this, "Email is not registered, " + "try signing up..!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TutorSignIn.this, TutorSignUp.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Log.e("Firebase", "Database error", databaseError.toException());
                    Toast.makeText(TutorSignIn.this, "Error validating credentials, " + "try again later..!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendUserToMainActivity(Tutor tutor) {
        Intent intent = new Intent(TutorSignIn.this, TutorDashboard.class);
        TutorApplication tutorApp = (TutorApplication) getApplication();
        tutorApp.tutor = tutor;
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