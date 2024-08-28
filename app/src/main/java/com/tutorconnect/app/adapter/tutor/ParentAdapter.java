package com.tutorconnect.app.adapter.tutor;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorconnect.app.R;
import com.tutorconnect.app.model.Parent;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.viewHolder> {

    private final Context mContext;
    private final List<Parent> parentList;
    private final OnParentActionListener onParentActionListener;

    public interface OnParentActionListener {
        void onRemoveParent(int position);
    }

    public ParentAdapter(Context mContext, List<Parent> parentList, OnParentActionListener onParentActionListener) {
        this.mContext = mContext;
        this.parentList = parentList;
        this.onParentActionListener = onParentActionListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_parent, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Parent model = parentList.get(position);
        holder.tv_parent_name.setText(model.getName());

        holder.btn_RemoveParent.setOnClickListener((v) -> {
            if (onParentActionListener != null) {
                onParentActionListener.onRemoveParent(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }

    public void removeItem(int position) {
        parentList.remove(position);
        notifyItemRemoved(position);
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_parent_name;
        private final Button btn_RemoveParent;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tv_parent_name = itemView.findViewById(R.id.tv_parent_name);
            btn_RemoveParent = itemView.findViewById(R.id.btn_remove_parent);
        }
    }
}
