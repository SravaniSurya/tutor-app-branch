package com.tutorconnect.app.student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tutorconnect.app.MainActivity;
import com.tutorconnect.app.R;
import com.tutorconnect.app.TutorApplication;
import com.tutorconnect.app.model.Student;

public class StudentDashboard extends AppCompatActivity {

    CardView btnViewAssignment;
    CardView btnViewNotes;
    CardView btnViewRemarks;
    CardView btnEditProfile;


    private boolean doubleBackToExitPressedOnce = false;
    private final Handler handler = new Handler();
    private final Runnable resetDoubleBackToExitPressedOnce = () -> doubleBackToExitPressedOnce = false;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        setTitle("Student - Dashboard");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnViewAssignment = findViewById(R.id.studentViewAssignment);
        btnViewNotes = findViewById(R.id.studentViewNotes);
        btnViewRemarks = findViewById(R.id.studentViewRemarks);
        btnEditProfile = findViewById(R.id.studentEditProfile);

        student = ((TutorApplication) getApplicationContext()).student;

        btnViewNotes.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, StudentViewNotes.class);
            startActivity(intent);
        });

        btnViewAssignment.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, StudentViewAssignment.class);
            startActivity(intent);
        });

        btnViewRemarks.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, StudentReview.class);
            startActivity(intent);
        });

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, StudentEditProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Handle the back button action
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(StudentDashboard.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        handler.postDelayed(resetDoubleBackToExitPressedOnce, 2000); // 2 seconds delay to reset
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tutor_app_menu, menu);
        return true;
    }
}