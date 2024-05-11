package com.example.pocketorganizer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayOfWeekAdapter extends RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder> {
    private List<DayOfWeek> dayOfWeekList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayOfWeekText;
        public TextView dayOfWeekNumberText;

        public ViewHolder(View view) {
            super(view);
            dayOfWeekText = view.findViewById(R.id.dayOfWeekText);
            dayOfWeekNumberText = view.findViewById(R.id.dayOfWeekNumberText);
        }
    }

    public DayOfWeekAdapter(List<DayOfWeek> dayOfWeekList) {
        this.dayOfWeekList = dayOfWeekList;
    }

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
    }

    @Override
    public int getItemCount() {
        return dayOfWeekList.size();
    }
}
