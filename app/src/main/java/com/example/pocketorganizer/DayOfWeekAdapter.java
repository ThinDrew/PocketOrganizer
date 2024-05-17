package com.example.pocketorganizer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketorganizer.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeekAdapter extends RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder> {
    private List<DayOfWeek> dayOfWeekList;
    private AppDatabase database;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayOfWeekText;
        public TextView dayOfWeekNumberText;
        public ImageButton addButton;

        public ViewHolder(View view) {
            super(view);
            dayOfWeekText = view.findViewById(R.id.dayOfWeekText);
            dayOfWeekNumberText = view.findViewById(R.id.dayOfWeekNumberText);
            addButton = view.findViewById(R.id.addNoteButton);
        }
    }

    public DayOfWeekAdapter(List<DayOfWeek> dayOfWeekList, AppDatabase database) {
        this.dayOfWeekList = dayOfWeekList;
        this.database = database;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_of_week_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DayOfWeek dayOfWeek = dayOfWeekList.get(position);
        holder.dayOfWeekText.setText(dayOfWeek.getName());
        holder.dayOfWeekNumberText.setText(dayOfWeek.getFormattedDate());

        if (dayOfWeek.isCurrentDay()) {
            holder.dayOfWeekText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.addButton.setBackgroundResource(R.drawable.custom_highlighted_add_button);
            LinearLayout linearLayout = holder.itemView.findViewById(R.id.dayOfWeekBackground);
            linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightBlue));
        }

        // Создание RecyclerView для заметок
        ArrayList<DailyNote> notes = dayOfWeek.getNotes();
        if (notes != null) {
            RecyclerView noteRecyclerView = holder.itemView.findViewById(R.id.noteRecyclerView);
            DailyNoteAdapter noteAdapter = new DailyNoteAdapter(notes);
            noteRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            noteRecyclerView.setAdapter(noteAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return dayOfWeekList.size();
    }
}
