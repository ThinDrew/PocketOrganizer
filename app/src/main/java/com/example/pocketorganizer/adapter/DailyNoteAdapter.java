package com.example.pocketorganizer.adapter;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketorganizer.R;
import com.example.pocketorganizer.database.AppDatabase;
import com.example.pocketorganizer.entities.Note;

import java.util.ArrayList;

public class DailyNoteAdapter extends RecyclerView.Adapter<DailyNoteAdapter.ViewHolder> {
    private ArrayList<Note> noteList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView descriptionText;
        public ImageButton checkButton;
        public View colorLine;

        public ViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.titleText);
            descriptionText = view.findViewById(R.id.descriptionText);
            checkButton = view.findViewById(R.id.checkButton);
            colorLine = view.findViewById(R.id.colorLine);
        }
    }

    public DailyNoteAdapter(ArrayList<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_note_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.titleText.setText(note.getTitle());
        holder.descriptionText.setText(note.getDescription());

        setNoteCompleted(holder, note.isChecked());

        holder.checkButton.setOnClickListener(view -> {
            note.check();
            setNoteCompleted(holder, note.isChecked());
            notifyDataSetChanged();
            updateNoteInDatabase(holder, note);
        });
    }

    private void setNoteCompleted(ViewHolder holder, boolean isChecked) {
        if (isChecked) {
            holder.titleText.setPaintFlags(holder.titleText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.titleText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightPurple));
            holder.descriptionText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
            holder.colorLine.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
            holder.checkButton.setBackgroundResource(R.drawable.check_circle_gray);
            LinearLayout linearLayout = holder.itemView.findViewById(R.id.noteBackground);
            linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grayBlue));
        } else {
            holder.titleText.setPaintFlags(holder.titleText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.titleText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.descriptionText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightPurple));
            holder.colorLine.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightBlue));
            holder.checkButton.setBackgroundResource(R.drawable.check_circle_blue);
            LinearLayout linearLayout = holder.itemView.findViewById(R.id.noteBackground);
            linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.extraDarkBlue));
        }
    }

    private void updateNoteInDatabase(ViewHolder holder, Note note) {
        new Thread(() -> AppDatabase.getInstance(holder.itemView.getContext()).noteDao().update(note)).start();
    }
    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
