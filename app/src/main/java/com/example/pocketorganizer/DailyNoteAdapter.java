package com.example.pocketorganizer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DailyNoteAdapter extends RecyclerView.Adapter<DailyNoteAdapter.ViewHolder> {
    private ArrayList<DailyNote> noteList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView descriptionText;

        public ViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.titleText);
            descriptionText = view.findViewById(R.id.descriptionText);
        }
    }

    public DailyNoteAdapter(ArrayList<DailyNote> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyNote note = noteList.get(position);
        holder.titleText.setText(note.getTitle());
        holder.descriptionText.setText(note.getDescription());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}