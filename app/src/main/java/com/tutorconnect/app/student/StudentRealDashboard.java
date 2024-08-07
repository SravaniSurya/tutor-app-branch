package com.tutorconnect.app.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tutorconnect.app.R;

public class StudentRealDashboard extends AppCompatActivity {

    CardView btnViewAssignment;
    CardView btnViewNotes;
    CardView btnViewRemarks;

    Intent intent;
    String studentName = "";
    String studentId = "";
    String tutorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_real_dashboard);

        setTitle("Student - Dashboard");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnViewAssignment = findViewById(R.id.studentViewAssignment);
        btnViewNotes = findViewById(R.id.studentViewNotes);
        btnViewRemarks = findViewById(R.id.studentViewRemarks);

        intent = getIntent();
        studentName = intent.getStringExtra("studentName");
        studentId = intent.getStringExtra("studentId");
        tutorId = intent.getStringExtra("tutorId");

        btnViewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, StudentViewNotes.class);
                intent.putExtra("studentName", studentName);
                intent.putExtra("studentId", studentId);
                intent.putExtra("tutorId", tutorId);
                startActivity(intent);
            }
        });

        btnViewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, ViewAssignment.class);
                intent.putExtra("studentName", studentName);
                intent.putExtra("studentId", studentId);
                intent.putExtra("tutorId", tutorId);
                startActivity(intent);
            }
        });

        btnViewRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, StudentDashboard.class);
                intent.putExtra("studentName", studentName);
                intent.putExtra("studentId", studentId);
                intent.putExtra("tutorId", tutorId);
                startActivity(intent);
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