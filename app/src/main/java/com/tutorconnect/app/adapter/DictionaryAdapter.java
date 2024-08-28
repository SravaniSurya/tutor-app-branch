package com.tutorconnect.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorconnect.app.R;
import com.tutorconnect.app.model.Dictionary;

import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MeaningViewHolder> {

    private List<Dictionary> meaningsList;

    public DictionaryAdapter(List<Dictionary> meaningsList) {
        this.meaningsList = meaningsList;
    }

    @NonNull
    @Override
    public MeaningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meaning, parent, false);
        return new MeaningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeaningViewHolder holder, int position) {
        Dictionary dictionary = meaningsList.get(position);

        holder.tvPartOfSpeech.setText(dictionary.getPartOfSpeech());
        holder.tvDefinition.setText(String.join("\n", dictionary.getDefinitions()));
    }

    @Override
    public int getItemCount() {
        return meaningsList != null ? meaningsList.size() : 0;
    }

    static class MeaningViewHolder extends RecyclerView.ViewHolder {
        TextView tvPartOfSpeech, tvDefinition;

        MeaningViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPartOfSpeech = itemView.findViewById(R.id.tvPartOfSpeech);
            tvDefinition = itemView.findViewById(R.id.tvDefinition);
        }
    }


}
