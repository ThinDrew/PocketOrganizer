package com.example.pocketorganizer.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketorganizer.R;
import com.example.pocketorganizer.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemAdapter.ToDoItemViewHolder> {

    private List<ToDoItem> toDoList;
    private List<ToDoItem> deletedToDoItems;
    public List<ToDoItem> tempToDoItems;
    private final SparseArray<ToDoItemViewHolder> viewHolders = new SparseArray<>();

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
        this.deletedToDoItems = new ArrayList<>();
        this.tempToDoItems = new ArrayList<>();
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
        setToDoCompleted(holder, toDoItem);

        viewHolders.put(position, holder);

        // Обработка кнопки выполнения дела
        holder.checkSquareButton.setOnClickListener(v -> {
            toDoItem.complete();
            setToDoCompleted(holder, toDoItem);
            updateToDo(toDoItem);
        });

        // Обработка кнопки удаления дела
        holder.deleteToDoButton.setOnClickListener(v -> deleteToDo(position, toDoItem));
    }

    private void deleteToDo(int position, ToDoItem toDoItem){
        // Сначала обновим все элементы в toDoList с текущими данными из EditText
        updateToDoItems();

        // Если это новое дело
        if (toDoItem.getId() < 0){
            tempToDoItems.remove(toDoItem);
        }
        // Иначе ещё и добавляем в список для удаления
        else{
            deletedToDoItems.add(toDoItem);
            tempToDoItems.remove(toDoItem);
        }

        // Удаляем элемент из основного списка
        toDoList.remove(position);

        // Удаляем соответствующий ViewHolder из SparseArray
        viewHolders.remove(position);

        // Обновляем позиции оставшихся ViewHolder в SparseArray
        for (int i = position; i < viewHolders.size(); i++) {
            int key = viewHolders.keyAt(i);
            ToDoItemViewHolder holder = viewHolders.get(key);
            viewHolders.remove(key);
            viewHolders.put(key - 1, holder);
        }

        // Уведомляем адаптер о том, что элемент был удален
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, toDoList.size());
    }

    private void updateToDo(ToDoItem newToDoItem){
        for (ToDoItem item : tempToDoItems){
            if (item.getId() == newToDoItem.getId()){
                int index = tempToDoItems.indexOf(item);
                tempToDoItems.set(index, newToDoItem);
                break;
            }
        }
    }

    private void setToDoCompleted(ToDoItemViewHolder holder, ToDoItem toDoItem){
        if (toDoItem.isCompleted()) {
            holder.checkSquareButton.setBackgroundResource(R.drawable.check_square_checked);
            holder.toDoEditText.setPaintFlags(holder.toDoEditText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.checkSquareButton.setBackgroundResource(R.drawable.check_square);
            holder.toDoEditText.setPaintFlags(holder.toDoEditText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    public void updateToDoItems() {
        for (int i = 0; i < viewHolders.size(); i++) {
            int key = viewHolders.keyAt(i);
            ToDoItemViewHolder holder = viewHolders.get(key);
            ToDoItem toDoItem = toDoList.get(key);
            if (holder != null && toDoItem != null) {
                toDoItem.setDescription(holder.toDoEditText.getText().toString());
                //вызываем метод для сохранения или обновления toDoItem
                updateToDo(toDoItem);
            }
        }
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public List<ToDoItem> getDeletedToDoItems(){
        return deletedToDoItems;
    }

    public List<ToDoItem> getTempToDoItems(){
        return tempToDoItems;
    }
}