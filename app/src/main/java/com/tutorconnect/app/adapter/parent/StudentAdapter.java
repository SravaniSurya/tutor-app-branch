package com.tutorconnect.app.adapter.parent;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorconnect.app.R;
import com.tutorconnect.app.model.Remark;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.viewHolder> {

    private final Context mContext;
    private final List<Remark> mList;

    public StudentAdapter(Context mContext, List<Remark> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.student_layout_for_parent,
                parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Remark model = mList.get(position);
        holder.tv_studentName.setText(model.getStudentName());
        holder.tv_studentSubject.setText(model.getStudentSubject());
        holder.tv_todayDate.setText(model.getDate());
        holder.tv_present.setText(model.isPresent() ? "Present" : "Absent");
        holder.rb_rating.setRating(Float.parseFloat(model.getRemarks()));

        holder.tv_present.setTextColor(model.isPresent() ?
                mContext.getResources().getColor(R.color.light_green) :
                mContext.getResources().getColor(R.color.light_red));

        if (model.getReview() == null) {
            holder.txt_remarks.setText("No review");
        } else {
            holder.txt_remarks.setText(model.getReview());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_studentName;
        private final TextView tv_studentSubject;
        private final TextView tv_todayDate;
        private final TextView tv_present;
        private final RatingBar rb_rating;
        private final TextView txt_remarks;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_studentName = itemView.findViewById(R.id.tv_studentName);
            tv_studentSubject = itemView.findViewById(R.id.tv_studentSubject);
            tv_todayDate = itemView.findViewById(R.id.tv_todayDate);
            tv_present = itemView.findViewById(R.id.tv_present);
            rb_rating = itemView.findViewById(R.id.rb_remarks);
            txt_remarks = itemView.findViewById(R.id.tv_remark);
        }
    }
}
