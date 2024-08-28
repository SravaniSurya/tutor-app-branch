package com.tutorconnect.app.student;

import android.app.ProgressDialog;
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
import com.tutorconnect.app.TutorApplication;
import com.tutorconnect.app.adapter.ViewAssignmentNotesAdapter;
import com.tutorconnect.app.model.Student;
import com.tutorconnect.app.model.AssignmentNotes;

import java.util.ArrayList;

public class StudentViewNotes extends AppCompatActivity {

    ArrayList<AssignmentNotes> arrayList = new ArrayList<>();
    ViewAssignmentNotesAdapter adapter;
    RecyclerView rvViewNotes;

    TextView tvNoData;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_notes);

        setTitle("Student - View Notes");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvViewNotes = findViewById(R.id.rv_viewNotes);
        tvNoData = findViewById(R.id.tv_no_data);

        rvViewNotes.setHasFixedSize(true);
        rvViewNotes.setLayoutManager(new LinearLayoutManager(StudentViewNotes.this));

        student = ((TutorApplication) getApplicationContext()).student;

        loadNotes();
    }

    private void loadNotes() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        Log.d("TAG1", "inside load notes");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("notes").orderByChild("tutorId").equalTo(student.getTutorId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AssignmentNotes model = dataSnapshot.getValue(AssignmentNotes.class);
                    arrayList.add(model);
                }

                adapter = new ViewAssignmentNotesAdapter(StudentViewNotes.this,
                        arrayList,  null);
                rvViewNotes.setAdapter(adapter);

                if (arrayList.isEmpty()) {
                    tvNoData.setVisibility(View.VISIBLE);
                    rvViewNotes.setVisibility(View.GONE);
                } else {
                    tvNoData.setVisibility(View.GONE);
                    rvViewNotes.setVisibility(View.VISIBLE);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error loading notes", Toast.LENGTH_SHORT).show();
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