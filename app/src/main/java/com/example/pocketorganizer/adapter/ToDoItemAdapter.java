package com.example.pocketorganizer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketorganizer.R;
import com.example.pocketorganizer.model.ToDoItem;

import java.util.List;

public class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemAdapter.ToDoItemViewHolder> {

    private List<ToDoItem> toDoList;
    private Context context;

    public static class ToDoItemViewHolder extends RecyclerView.ViewHolder {

        EditText toDoEditText;
        ImageButton checkSquareButton;
        ImageButton deleteToDoButton;

        public ToDoItemViewHolder(View itemView) {
            super(itemView);
            toDoEditText = itemView.findViewById(R.id.toDoEditText);
            checkSquareButton = itemView.findViewById(R.id.checkSquareButton);
            deleteToDoButton = itemView.findViewById(R.id.deleteToDoButton);
        }
    }

    public ToDoItemAdapter(Context context, List<ToDoItem> toDoList) {
        this.context = context;
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public ToDoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        return new ToDoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoItemViewHolder holder, int position) {
        ToDoItem toDoItem = toDoList.get(position);
        holder.toDoEditText.setText(toDoItem.getDescription());

        holder.deleteToDoButton.setOnClickListener(v -> {
            toDoList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, toDoList.size());
        });

        holder.checkSquareButton.setOnClickListener(v -> {
            toDoItem.complete();
            if (toDoItem.isCompleted())
                holder.checkSquareButton.setBackgroundResource(R.drawable.check_square_checked);
            else
                holder.checkSquareButton.setBackgroundResource(R.drawable.check_square);
        });
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}