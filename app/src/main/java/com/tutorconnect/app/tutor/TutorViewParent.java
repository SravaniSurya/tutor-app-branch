package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.tutorconnect.app.adapter.tutor.ParentAdapter;
import com.tutorconnect.app.model.Parent;
import com.tutorconnect.app.model.Tutor;

import java.util.ArrayList;
import java.util.List;

public class TutorViewParent extends AppCompatActivity implements ParentAdapter.OnParentActionListener {

    Intent intent;

    String studentId = "";
    String studentName = "";

    DatabaseReference dbReference;

    List<Parent> parentList = new ArrayList<>();
    List<DataSnapshot> parentSnapshots = new ArrayList<>();

    RecyclerView recyclerView;
    ParentAdapter mAdapter;

    ProgressDialog progressDialog;

    Tutor tutor;

    // Existing variables
    Button btnAddParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view_parent);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        recyclerView = findViewById(R.id.rv_showAllParents);
        btnAddParent = findViewById(R.id.btn_add_parent);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TutorViewParent.this));

        intent = getIntent();
        studentId = intent.getStringExtra("studentId");
        studentName = intent.getStringExtra("studentName");

        setTitle("Parent - " + studentName);

        dbReference = FirebaseDatabase.getInstance().getReference();

        tutor = ((TutorApplication) getApplicationContext()).tutor;

        getAllParents();

        btnAddParent.setOnClickListener((v) -> onClickAddNewParent());
    }

    private void getAllParents() {
        progressDialog.show();

        String compositeKey = tutor.getId() + "_" + studentId;

        Query emailQuery = dbReference.child("parents")
                .orderByChild("compositeKey").equalTo(compositeKey);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Parent model = dataSnapshot.getValue(Parent.class);
                    Log.d("TAG", "onDataChange: " + model.getName());
                    parentList.add(model);
                    parentSnapshots.add(dataSnapshot);
                }
                mAdapter = new ParentAdapter(TutorViewParent.this, parentList,
                        TutorViewParent.this);
                recyclerView.setAdapter(mAdapter);

                toggleEmptyView();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error loading parent",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void toggleEmptyView() {
        if (parentList.isEmpty()) {
            btnAddParent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            btnAddParent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRemoveParent(int position) {
        progressDialog.show();

        parentSnapshots.get(position).getRef().removeValue()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        mAdapter.removeItem(position);
                        Toast.makeText(getApplicationContext(), "Parent removed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error removing parent", Toast.LENGTH_SHORT).show();
                    }
                    toggleEmptyView();
                });
    }

    private void onClickAddNewParent() {
        Intent intent = new Intent(TutorViewParent.this, AddParent.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Handle the back button action
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
