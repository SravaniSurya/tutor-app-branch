package com.tutorconnect.app.parent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
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
import com.tutorconnect.app.MainActivity;
import com.tutorconnect.app.R;
import com.tutorconnect.app.TutorApplication;
import com.tutorconnect.app.adapter.parent.StudentAdapter;
import com.tutorconnect.app.model.Parent;
import com.tutorconnect.app.model.Remark;
import com.tutorconnect.app.model.Student;
import com.tutorconnect.app.tutor.TutorSignIn;

import java.util.ArrayList;
import java.util.List;

public class ParentDashboard extends AppCompatActivity {

    List<Remark> mList = new ArrayList<>();

    RecyclerView recyclerView;
    StudentAdapter mAdapter;

    TextView tvNoData;

    private boolean doubleBackToExitPressedOnce = false;
    private final Handler handler = new Handler();
    private final Runnable resetDoubleBackToExitPressedOnce = () -> doubleBackToExitPressedOnce = false;

    Parent parent;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        setTitle("Parent - Dashboard");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.rv_showAllAttendance);
        tvNoData = findViewById(R.id.tv_no_data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        parent = ((TutorApplication) getApplicationContext()).parent;

        getAllAttendance();
    }

    private void getAllAttendance() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        // get all students with logged in parent
        dbRef.child("students").orderByChild("parentId").equalTo(parent.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> studentIds = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Student student = dataSnapshot.getValue(Student.class);
                            studentIds.add(student.getId());
                        }

                        filterAttendance(progressDialog, studentIds);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error loading", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void filterAttendance(ProgressDialog progressDialog, List<String> studentIds) {

        for (String studentId : studentIds) {
            Query attendanceQuery = dbRef.child("attendance")
                    .child(studentId);

            attendanceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    mList.clear();

                    for (DataSnapshot attendanceSnapshot : snapshot.getChildren()) {
                        Remark remark = attendanceSnapshot.getValue(Remark.class);
                        mList.add(remark);
                    }

                    mAdapter = new StudentAdapter(ParentDashboard.this, mList);
                    recyclerView.setAdapter(mAdapter);

                    if (mList.isEmpty()) {
                        tvNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors
                    Toast.makeText(ParentDashboard.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Handle the back button action
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(ParentDashboard.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.edit_profile) {
            Intent intent = new Intent(ParentDashboard.this, ParentEditProfileActivity.class);
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
        getMenuInflater().inflate(R.menu.tutor_parent_app_menu, menu);
        return true;
    }
}