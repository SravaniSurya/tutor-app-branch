package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.tutorconnect.app.adapter.parent.StudentAdapter;
import com.tutorconnect.app.model.Remark;
import com.tutorconnect.app.model.Student;
import com.tutorconnect.app.model.Tutor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceFragment extends Fragment {

    List<Remark> mList = new ArrayList<>();
    RecyclerView recyclerView;
    StudentAdapter mAdapter;
    TextView tvNoData;

    DatabaseReference dbRef;
    Tutor tutor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_view_attendance, container, false);

        tutor = ((TutorApplication) requireActivity().getApplicationContext()).tutor;

        // Initialize Firebase reference
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Bind views
        recyclerView = view.findViewById(R.id.rv_showAllAttendance);
        tvNoData = view.findViewById(R.id.tv_no_data);

        // Set up RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fetch and display attendance data
        getAllStudents();
    }

    private void getAllStudents() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        Query query = dbRef.child("students")
                .orderByChild("tutorId").equalTo(tutor.getId());

        // Get all students associated with the logged-in tutor
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> studentIds = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    studentIds.add(student.getId());
                }

                LocalDate currentDate = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentDate = LocalDate.now();
                }
                assert currentDate != null;
                String date = currentDate.toString();

                filterAttendanceWithDate(progressDialog, studentIds, date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void filterAttendanceWithDate(ProgressDialog progressDialog, List<String> studentIds, String date) {
        mList.clear();
        for (String studentId : studentIds) {
            Query attendanceQuery = dbRef.child("attendance")
                    .child(studentId)
                    .child(date);

            attendanceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Remark remark = snapshot.getValue(Remark.class);
                    if (remark != null) {
                        mList.add(remark);
                    }

                    mAdapter = new StudentAdapter(getContext(), mList);
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
                    Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tutor_app_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
