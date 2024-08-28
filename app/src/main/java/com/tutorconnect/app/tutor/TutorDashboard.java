package com.tutorconnect.app.tutor;

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
import com.tutorconnect.app.model.Tutor;

public class TutorDashboard extends AppCompatActivity {

    CardView btn_viewStudents;
    CardView btn_viewAssignment;
    CardView btn_viewNotes;
    CardView btn_uploadNotes;
    CardView btn_uploadAssignment;
    CardView btn_editProfile;

    Intent intent;

    Tutor tutor;

    private boolean doubleBackToExitPressedOnce = false;
    private final Handler handler = new Handler();
    private final Runnable resetDoubleBackToExitPressedOnce = () -> doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_dashboard);

        setTitle("Tutor - Dashboard");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tutor = ((TutorApplication) getApplicationContext()).tutor;

        btn_viewStudents = findViewById(R.id.cv_viewStudents);
        btn_viewAssignment = findViewById(R.id.cv_viewAssignment);
        btn_viewNotes = findViewById(R.id.cv_viewNotes);
        btn_uploadNotes = findViewById(R.id.cv_uploadNotes);
        btn_uploadAssignment = findViewById(R.id.cv_uploadAssignment);
        btn_editProfile = findViewById(R.id.cv_editProfile);

        intent = getIntent();
        btn_viewStudents.setOnClickListener(v -> {
            Intent intent = new Intent(TutorDashboard.this, TutorViewStudents.class);
            startActivity(intent);
        });

        btn_viewAssignment.setOnClickListener(v -> {
            Intent intent = new Intent(TutorDashboard.this, TutorViewAssignmentActivity.class);
            startActivity(intent);
        });

        btn_uploadNotes.setOnClickListener(v -> {
            Intent intent = new Intent(TutorDashboard.this, TutorUploadNotesActivity.class);
            startActivity(intent);
        });
        btn_uploadAssignment.setOnClickListener(v -> {
            Intent intent = new Intent(TutorDashboard.this, TutorUploadAssignment.class);
            startActivity(intent);
        });

        btn_viewNotes.setOnClickListener(v -> {
            Intent intent = new Intent(TutorDashboard.this, TutorViewNotes.class);
            startActivity(intent);
        });

        btn_editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(TutorDashboard.this, TutorEditProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Handle the back button action
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.logout) {
            Intent intent = new Intent(TutorDashboard.this, MainActivity.class);
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