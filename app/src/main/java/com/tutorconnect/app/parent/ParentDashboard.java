package com.tutorconnect.app.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.model.Parent;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.parent.StudentAdapter;
import com.tutorconnect.app.tutor.TutorSignUp;

import java.util.ArrayList;
import java.util.List;

public class ParentDashboard extends AppCompatActivity {

    List<Parent> mList = new ArrayList<>();
    RecyclerView recyclerView;
    StudentAdapter mAdapter;
    Intent intent;
    String parentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        setTitle("Dashboard");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.rv_showAllAttendance);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        intent = getIntent();
        parentName = intent.getStringExtra("parentName");

        getAllAttendance();

    }

    private void getAllAttendance() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(TutorSignUp.ATTENDANCE).child(parentName);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Parent model = dataSnapshot.getValue(Parent.class);
                        mList.add(model);
                    }
                    mAdapter = new StudentAdapter(ParentDashboard.this, mList);
                    recyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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