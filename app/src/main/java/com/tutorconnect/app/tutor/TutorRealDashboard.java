package com.tutorconnect.app.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tutorconnect.app.R;

public class TutorRealDashboard extends AppCompatActivity {

    CardView btn_viewStudents;
    CardView btn_viewAssignment;
    CardView btn_viewNotes;
    CardView btn_uploadNotes;
    CardView btn_uploadAssignment;

    Intent intent;

    String tutorId, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_real_dashboard);

        setTitle("Tutor");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btn_viewStudents = findViewById(R.id.cv_viewStudents);
        btn_viewAssignment = findViewById(R.id.cv_viewAssignment);
        btn_viewNotes = findViewById(R.id.cv_viewNotes);
        btn_uploadNotes = findViewById(R.id.cv_uploadNotes);
        btn_uploadAssignment = findViewById(R.id.cv_uploadAssignment);

        intent = getIntent();
        email = intent.getStringExtra("email");
        tutorId = intent.getStringExtra("tutorId");
        password = intent.getStringExtra("password");

        btn_viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, TutorDashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("tutorId", tutorId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_uploadNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, AddNotes.class);
                intent.putExtra("userId", tutorId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_uploadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, uploadAssignment.class);
                intent.putExtra("userId", tutorId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, ViewNotes.class);
                intent.putExtra("userId", tutorId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button action
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}