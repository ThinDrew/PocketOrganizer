package com.example.pocketorganizer.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.pocketorganizer.DayOfWeek;
import com.example.pocketorganizer.R;
import com.example.pocketorganizer.activity.AddNoteActivity;
import com.example.pocketorganizer.activity.MainActivity;
import com.example.pocketorganizer.database.AppDatabase;
import com.example.pocketorganizer.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeekAdapter extends RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder> {
    private List<DayOfWeek> dayOfWeekList;

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
        else {
            holder.dayOfWeekText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightBlue));
            holder.addButton.setBackgroundResource(R.drawable.custom_add_button);
            LinearLayout linearLayout = holder.itemView.findViewById(R.id.dayOfWeekBackground);
            linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.blue));
        }

        // Создание RecyclerView для заметок
        ArrayList<Note> notes = dayOfWeek.getNotes();
        if (notes != null) {
            RecyclerView noteRecyclerView = holder.itemView.findViewById(R.id.noteRecyclerView);
            DailyNoteAdapter noteAdapter = new DailyNoteAdapter(notes);
            noteRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            noteRecyclerView.setAdapter(noteAdapter);
        }

        holder.addButton.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), AddNoteActivity.class);
            String date = dayOfWeekList.get(position).getDate().toString();
            intent.putExtra("noteDate", date);
            ((MainActivity) holder.itemView.getContext()).startActivityForResult(intent, MainActivity.REQUEST_CODE_ADD_NOTE);
        });
    }

    @Override
    public int getItemCount() {
        return dayOfWeekList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<DayOfWeek> newDayOfWeekList) {
        this.dayOfWeekList.clear();
        this.dayOfWeekList.addAll(newDayOfWeekList);
        notifyDataSetChanged();
    }
}
