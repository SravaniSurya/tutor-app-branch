package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.ViewAssignmentNotesAdapter;
import com.tutorconnect.app.model.AssignmentNotes;

import java.util.ArrayList;

public class TutorViewSubmittedAssignments extends AppCompatActivity {

    ArrayList<AssignmentNotes> arrayList = new ArrayList<>();
    ViewAssignmentNotesAdapter adapter;
    RecyclerView rvViewAssignments;

    TextView tvNoData;

    String studentId;
    String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view_submitted_assignments);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvViewAssignments = findViewById(R.id.rv_viewAssignment);
        tvNoData = findViewById(R.id.tv_no_data);

        Intent intent = getIntent();
        studentId = intent.getStringExtra("studentId");
        studentName = intent.getStringExtra("studentName");

        setTitle(studentName + " - assignments");

        rvViewAssignments.setHasFixedSize(true);
        rvViewAssignments.setLayoutManager(new LinearLayoutManager(TutorViewSubmittedAssignments.this));

        loadAssignments();
    }


    private void loadAssignments() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("completed_assignments").orderByChild("studentId").equalTo(studentId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AssignmentNotes model = dataSnapshot.getValue(AssignmentNotes.class);
                    arrayList.add(model);
                }
                Log.d("TAG1", "arraylist size : " + arrayList.size());
                adapter = new ViewAssignmentNotesAdapter(TutorViewSubmittedAssignments.this, arrayList, null);
                rvViewAssignments.setAdapter(adapter);

                if (arrayList.isEmpty()) {
                    tvNoData.setVisibility(View.VISIBLE);
                    rvViewAssignments.setVisibility(View.GONE);
                } else {
                    tvNoData.setVisibility(View.GONE);
                    rvViewAssignments.setVisibility(View.VISIBLE);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error loading assignments", Toast.LENGTH_SHORT).show();
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