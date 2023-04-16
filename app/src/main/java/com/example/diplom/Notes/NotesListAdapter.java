package com.example.diplom.Notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.R;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    Context context;
    List<Notes> list;

    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.noteTitle.setText(list.get(position).getTitle());
        holder.noteTitle.setSelected(true);

        holder.noteContent.setText(list.get(position).getNotes());
        holder.noteContent.setSelected(true);

        holder.noteDate.setText(list.get(position).getDate());
        holder.noteDate.setSelected(true);

        if (list.get(position).isPinned()) {
            holder.notePin.setImageResource(R.drawable.pin_blue);
        } else {
            holder.notePin.setImageResource(0);
        }
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.teal_200, null));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList (List<Notes> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notes_container;
    TextView noteTitle, noteContent, noteDate;
    ImageView notePin;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_container = itemView.findViewById(R.id.notes_container);
        noteTitle = itemView.findViewById(R.id.noteTitle);
        noteContent = itemView.findViewById(R.id.noteContent);
        noteDate = itemView.findViewById(R.id.noteDate);
        notePin = itemView.findViewById(R.id.notePin);

    }

}
