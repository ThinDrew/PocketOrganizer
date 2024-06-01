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
import com.example.pocketorganizer.activity.NoteEditorActivity;
import com.example.pocketorganizer.activity.MainActivity;
import com.example.pocketorganizer.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeekAdapter extends RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder> {
    private List<DayOfWeek> dayOfWeekList;
    private List<Note> somedayNotes;
    private static final int TYPE_DAY_OF_WEEK = 0;
    private static final int TYPE_SOMEDAY = 1;

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

    public DayOfWeekAdapter(List<DayOfWeek> dayOfWeekList) {
        this.dayOfWeekList = dayOfWeekList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < dayOfWeekList.size()) {
            return TYPE_DAY_OF_WEEK;
        } else {
            return TYPE_SOMEDAY;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_of_week_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Если это обычный день недели
        if (getItemViewType(position) == TYPE_DAY_OF_WEEK) {
            DayOfWeek dayOfWeek = dayOfWeekList.get(position);
            holder.dayOfWeekText.setText(dayOfWeek.getName());
            holder.dayOfWeekNumberText.setText(dayOfWeek.getFormattedDate());

            setDayToCurrent(holder, dayOfWeek.isCurrentDay());

            // Создание RecyclerView для заметок
            ArrayList<Note> notes = dayOfWeek.getNotes();
            if (notes != null) {
                RecyclerView noteRecyclerView = holder.itemView.findViewById(R.id.noteRecyclerView);
                DailyNoteAdapter noteAdapter = new DailyNoteAdapter(notes);
                noteRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
                noteRecyclerView.setAdapter(noteAdapter);
            }

            // Обработка кнопки для добавления заметки
            holder.addButton.setOnClickListener(view -> {
                Intent intent = new Intent(holder.itemView.getContext(), NoteEditorActivity.class);
                String date = dayOfWeekList.get(position).getDate().toString();
                intent.putExtra("noteDate", date);
                ((MainActivity) holder.itemView.getContext()).startActivityForResult(intent, MainActivity.REQUEST_CODE_EDIT_NOTE);
            });
        }
        // Если это секция "Когда-нибудь"
        else {
            holder.dayOfWeekText.setText("Когда-нибудь");
            holder.dayOfWeekNumberText.setText("");
            // Создание RecyclerView для заметок
            ArrayList<Note> notes = (ArrayList<Note>)somedayNotes;
            if (notes != null) {
                RecyclerView noteRecyclerView = holder.itemView.findViewById(R.id.noteRecyclerView);
                DailyNoteAdapter noteAdapter = new DailyNoteAdapter(notes);
                noteRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
                noteRecyclerView.setAdapter(noteAdapter);
            }

            // Обработка кнопки для добавления заметки
            holder.addButton.setOnClickListener(view -> {
                Intent intent = new Intent(holder.itemView.getContext(), NoteEditorActivity.class);
                intent.putExtra("noteDate", "Someday");
                ((MainActivity) holder.itemView.getContext()).startActivityForResult(intent, MainActivity.REQUEST_CODE_EDIT_NOTE);
            });
        }
    }

    private void setDayToCurrent(DayOfWeekAdapter.ViewHolder holder, boolean isCurrent) {
        if (isCurrent) {
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
    }

    @Override
    public int getItemCount() {
        // +1 для секции "Когда-нибудь"
        return dayOfWeekList.size() + 1;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<DayOfWeek> newDayOfWeekList, List<Note> somedayNotes) {
        this.dayOfWeekList.clear();
        this.dayOfWeekList.addAll(newDayOfWeekList);
        this.somedayNotes = somedayNotes;
        notifyDataSetChanged();
    }
}
