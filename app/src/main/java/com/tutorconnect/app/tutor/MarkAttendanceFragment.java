package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.tutorconnect.app.adapter.tutor.StudentAdapter;
import com.tutorconnect.app.model.Student;
import com.tutorconnect.app.model.Tutor;

import java.util.ArrayList;
import java.util.List;

public class MarkAttendanceFragment extends Fragment {

    DatabaseReference dbReference;

    List<Student> mList = new ArrayList<>();
    RecyclerView recyclerView;
    StudentAdapter mAdapter;
    TextView tvNoData;

    Tutor tutor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mark_attendance, container, false);

        tutor = ((TutorApplication) requireActivity().getApplicationContext()).tutor;


        recyclerView = view.findViewById(R.id.rv_showAllStudents);
        tvNoData = view.findViewById(R.id.tv_no_data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        dbReference = FirebaseDatabase.getInstance().getReference();

        getAllStudent();

        return view;
    }

    private void getAllStudent() {

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        Query emailQuery = dbReference.child("students")
                .orderByChild("tutorId").equalTo(tutor.getId());

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student model = dataSnapshot.getValue(Student.class);
                    Log.d("TAG", "onDataChange: " + model.getName());
                    mList.add(model);
                }
                mAdapter = new StudentAdapter(requireContext(), mList, tutor.getId());
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
                Toast.makeText(requireContext(), "Error loading students", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
