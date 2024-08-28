package com.tutorconnect.app.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorconnect.app.PdfViewActivity;
import com.tutorconnect.app.R;
import com.tutorconnect.app.model.Student;
import com.tutorconnect.app.model.AssignmentNotes;
import com.tutorconnect.app.student.StudentUploadAssignment;
import com.tutorconnect.app.student.StudentViewAssignment;

import java.util.ArrayList;

public class ViewAssignmentNotesAdapter extends
        RecyclerView.Adapter<ViewAssignmentNotesAdapter.viewholder> {

    Context context;
    ArrayList<AssignmentNotes> mList;
    private final ProgressDialog progressDialog;
    Student student;


    public ViewAssignmentNotesAdapter(Context context, ArrayList<AssignmentNotes> mList,
                                      Student student) {
        this.context = context;
        this.mList = mList;
        this.student = student;

        progressDialog = new ProgressDialog(context);
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.assignment_notes_layout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        AssignmentNotes assignmentNotes = mList.get(position);
        Log.d("TAG1", "onBindViewHolder: " + assignmentNotes);
        holder.textView.setText(assignmentNotes.getName());

        if(student != null) { // if student object is passed show upload assignmentNotes button.
            holder.btnUploadAssignment.setVisibility(View.VISIBLE);
            holder.btnUploadAssignment.setOnClickListener(v -> {
                context.startActivity(new Intent(context, StudentUploadAssignment.class));
            });
        } else {
            holder.btnUploadAssignment.setVisibility(View.GONE);
        }

        holder.rootLayout.setOnClickListener((v) -> PdfViewActivity.start(context, assignmentNotes.getUrl()));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView textView;
        Button btnUploadAssignment;
        RelativeLayout rootLayout;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textview);
            btnUploadAssignment = itemView.findViewById(R.id.btn_uploadAssignment);
            rootLayout = itemView.findViewById(R.id.notes_root_layout);
        }
    }
}
