package com.example.pocketorganizer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

    public DayOfWeekAdapter(List<DayOfWeek> dayOfWeekList) {
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
        holder.dayOfWeekNumberText.setText(dayOfWeek.getNumberAndMonth());

        if (dayOfWeek.isCurrentDay()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightBlue));  // Устанавливаем голубой фон
            holder.dayOfWeekText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white)); // Устанавливаем белый цвет текста
            holder.addButton.setBackgroundResource(R.drawable.custom_highlighted_add_button);
        }
    }

    @Override
    public int getItemCount() {
        return dayOfWeekList.size();
    }
}
