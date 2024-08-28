package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutorconnect.app.R;
import com.tutorconnect.app.TutorApplication;
import com.tutorconnect.app.model.Tutor;

import java.util.HashMap;
import java.util.Map;

public class TutorPasswordChangeActivity extends AppCompatActivity {

    EditText et_confirm_password, et_password;
    Button btn_change_password;

    DatabaseReference dbReference;

    ProgressDialog progressDialog;

    Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_change_password);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tutor = ((TutorApplication) getApplicationContext()).tutor;

        setTitle("Tutor - password change");

        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_password = findViewById(R.id.et_password);
        btn_change_password = findViewById(R.id.btn_change);

        dbReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        btn_change_password.setOnClickListener(v -> performPasswordChange());
    }

    private void performPasswordChange() {
        String password = et_password.getText().toString();
        String confirmPassword = et_confirm_password.getText().toString();

        if (password.isEmpty()) {
            et_password.setError("Please Enter Password");
        } else if (confirmPassword.isEmpty()) {
            et_confirm_password.setError("Please Confirm Password");
        } else if (password.length() < 6) {
            et_password.setError("Password too short");
        } else if (!password.equals(confirmPassword)) {
            et_password.setError("Password mismatch");
            et_confirm_password.setError("Password mismatch");
        } else {
            progressDialog.setMessage("Validating credentials....");
            progressDialog.setTitle("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // update password and mark require password change to false
            // Create a map of the new values
            Map<String, Object> userValues = new HashMap<>();
            userValues.put("password", password);

            dbReference.child("tutors").child(tutor.getId()).updateChildren(userValues)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Update successful
                            Toast.makeText(TutorPasswordChangeActivity.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                            onPasswordChangedSuccessfully();
                        } else {
                            // Update failed
                            Toast.makeText(TutorPasswordChangeActivity.this, "Failed to change password, try again later", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(TutorPasswordChangeActivity.this, "Failed to change password, try again later", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void onPasswordChangedSuccessfully() {
        Intent intent = new Intent(TutorPasswordChangeActivity.this, TutorDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}