package com.tutorconnect.app.adapter.tutor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutorconnect.app.R;
import com.tutorconnect.app.model.Remark;
import com.tutorconnect.app.model.Student;
import com.tutorconnect.app.tutor.TutorViewParent;
import com.tutorconnect.app.tutor.TutorViewSubmittedAssignments;

import java.time.LocalDate;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.viewHolder> {

    private final Context mContext;
    private final List<Student> studentList;
    private Float studentRemarks = 0.0f;
    private Boolean studentAttendance = false;
    private final String tutorId;
    private String studentReview = "";

    private final ProgressDialog progressDialog;

    public StudentAdapter(Context mContext, List<Student> studentList, String tutorId) {
        this.mContext = mContext;
        this.studentList = studentList;
        this.tutorId = tutorId;
        progressDialog = new ProgressDialog(mContext);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_student, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Student model = studentList.get(position);
        holder.tv_studentName.setText(model.getName());
        holder.tv_studentSubject.setText(model.getSubject());
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.toString();
        holder.tv_todayDate.setText(date);

        holder.btn_present.setOnClickListener(v -> {
            progressDialog.setMessage("Marking Attendance");
            progressDialog.setTitle("Attendance....");
            progressDialog.show();
            studentAttendance = true;
            markAttendance(model, date);
        });
        holder.btn_absent.setOnClickListener(v -> {
            progressDialog.setMessage("Marking Attendance");
            progressDialog.setTitle("Attendance....");
            progressDialog.show();
            studentAttendance = false;
            markAttendance(model, date);
        });
        holder.btn_remarks.setOnClickListener(v -> {
            progressDialog.setMessage("Marking Attendance");
            progressDialog.setTitle("Attendance....");
            progressDialog.show();
            createDialog(model, date);
        });

        holder.btn_view_parents.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, TutorViewParent.class);
            intent.putExtra("tutorId", tutorId);
            intent.putExtra("studentId", model.getId());
            intent.putExtra("studentName", model.getName());
            mContext.startActivity(intent);
        });

        holder.btn_view_assignments.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, TutorViewSubmittedAssignments.class);
            intent.putExtra("studentId", model.getId());
            intent.putExtra("studentName", model.getName());
            mContext.startActivity(intent);
        });
    }

    private void createDialog(Student model, String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_remarks, null, false);
        builder.setView(view);

        RatingBar ratingBar = view.findViewById(R.id.rating_studentRemarks);
        TextView tvRatingFeedback = view.findViewById(R.id.tv_ratingFeedback);
        TextView etRemarks = view.findViewById(R.id.et_remarks); // Adding a TextView for remarks input
        AlertDialog alertDialog = builder.create();
        Button btn_giveRemarks = view.findViewById(R.id.btn_giveRemarks);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            String feedback;
            switch ((int) rating) {
                case 5:
                    feedback = "Excellent";
                    break;
                case 4:
                    feedback = "Very Good";
                    break;
                case 3:
                    feedback = "Good";
                    break;
                case 2:
                    feedback = "Fair";
                    break;
                case 1:
                    feedback = "Poor";
                    break;
                default:
                    feedback = "No Rating";
                    break;
            }
            tvRatingFeedback.setText(feedback);
        });

        alertDialog.show();
        btn_giveRemarks.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String remarks = etRemarks.getText().toString(); // Get remarks text input
            progressDialog.setMessage("Adding Your Remarks");
            progressDialog.setTitle("Adding...");
            progressDialog.setCanceledOnTouchOutside(false);
            studentRemarks = rating;
            studentReview = remarks; // Store the remarks string
            markAttendance(model, date);
            alertDialog.dismiss();
        });
    }


    private void markAttendance(Student model, String date) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("attendance")
                .child(model.getId())
                .child(date);

        Remark remark = new Remark(model.getName(), model.getSubject(), studentAttendance, date,
                studentRemarks.toString(), studentReview, model.getId(),
                model.getTutorId(), model.getParentId());

        reference.setValue(remark).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(mContext, "Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_studentName;
        private final TextView tv_studentSubject;
        private final TextView tv_todayDate;
        private final Button btn_present;
        private final Button btn_absent;
        private final Button btn_remarks;
        private final Button btn_view_parents;
        private final Button btn_view_assignments;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_studentName = itemView.findViewById(R.id.tv_studentName);
            tv_studentSubject = itemView.findViewById(R.id.tv_studentSubject);
            tv_todayDate = itemView.findViewById(R.id.tv_todayDate);
            btn_present = itemView.findViewById(R.id.btn_present);
            btn_absent = itemView.findViewById(R.id.btn_absent);
            btn_remarks = itemView.findViewById(R.id.btn_remarks);
            btn_view_parents = itemView.findViewById(R.id.btn_view_parents);
            btn_view_assignments = itemView.findViewById(R.id.btn_view_assignments);
        }
    }

}
