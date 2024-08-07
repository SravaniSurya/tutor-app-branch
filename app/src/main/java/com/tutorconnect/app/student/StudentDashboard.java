package com.tutorconnect.app.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.parent.StudentAdapter;
import com.tutorconnect.app.model.Parent;
import com.tutorconnect.app.tutor.TutorSignUp;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboard extends AppCompatActivity {
    List<Parent> mList = new ArrayList<>();
    RecyclerView recyclerView;
    StudentAdapter mAdapter;

    Intent intent;
    String studentName = "";
    String studentId = "";
    String tutorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        setTitle("Student - Dashboard");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.rv_showAllAttendance);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        intent = getIntent();
        studentName = intent.getStringExtra("studentName");
        studentId = intent.getStringExtra("studentId");
        tutorId = intent.getStringExtra("tutorId");

        getAllAttendance();
    }

    private void getAllAttendance() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Attendance")
                .child(studentName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Parent model = dataSnapshot.getValue(Parent.class);
                    mList.add(model);
                }
                mAdapter = new StudentAdapter(StudentDashboard.this, mList);
                recyclerView.setAdapter(mAdapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error loading reviews", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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