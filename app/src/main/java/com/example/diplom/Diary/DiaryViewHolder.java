package com.example.diplom.Diary;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    public final TextView rating;
    public final ImageView emotions;
    private final DiaryAdapter.OnItemListener onItemListener;
    public DiaryViewHolder(@NonNull View itemView, DiaryAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        rating = itemView.findViewById(R.id.cellDayRating);
        emotions = itemView.findViewById(R.id.cellDayEmotions);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.days = days;
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }

}
