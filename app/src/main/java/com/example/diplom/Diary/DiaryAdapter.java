package com.example.diplom.Diary;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryViewHolder> {

    private final Activity activity;
    private final ArrayList<LocalDate> days;
    private Entries entry;
    private final OnItemListener onItemListener;
    private final int cellView;
    private EntriesDB database;

    public DiaryAdapter(Activity activity, ArrayList<LocalDate> days, OnItemListener onItemListener, int cellView) {
        this.activity = activity;
        this.days = days;
        this.onItemListener = onItemListener;
        this.cellView = cellView;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);

        return new DiaryViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        database = EntriesDB.getInstance(activity);
        entry = database.entriesDAO().getDay(CalendarUtils.formattedDate(date));

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        if(date.equals(CalendarUtils.selectedDate)) {
            holder.eventState.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.state_0_gray));
        } else if(date.equals(LocalDate.now())) {
            holder.eventState.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.state_0_yellow));
        } else {
            holder.eventState.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.state_0_white));
        }

        if (entry != null) {
            holder.rating.setText(entry.getRating());
        } else {
            holder.rating.setText("0.0");
        }
        holder.rating.setSelected(true);

        if (entry != null) {

            if (entry.getEmotion().equals("0")) {
                holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_0));
            } else if (entry.getEmotion().equals("1")) {
                holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_1));
            } else if (entry.getEmotion().equals("2")) {
                holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_2));
            } else if (entry.getEmotion().equals("3")) {
                holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_3));
            } else if (entry.getEmotion().equals("4")) {
                holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_4));
            } else if (entry.getEmotion().equals("5")) {
                holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_5));
            } else if (entry.getEmotion().equals("6")) {
                holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_6));
            } else if (entry.getEmotion().equals("7")) {
                holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_7));
            }
        } else {
            holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.mood_0));
        }
        holder.emotions.setSelected(true);

        if (cellView == 0) {
            holder.dayOfMonth.setVisibility(View.VISIBLE);
            holder.emotions.setVisibility(View.GONE);
            holder.rating.setVisibility(View.GONE);
        } else if (cellView == 1) {
            holder.dayOfMonth.setVisibility(View.GONE);
            holder.emotions.setVisibility(View.VISIBLE);
            holder.rating.setVisibility(View.GONE);
        } else {
            holder.dayOfMonth.setVisibility(View.GONE);
            holder.emotions.setVisibility(View.GONE);
            holder.rating.setVisibility(View.VISIBLE);
        }

        if(date.equals(CalendarUtils.selectedDate)) {
            holder.parentView.setBackgroundColor(Color.LTGRAY);
        } else if(date.equals(LocalDate.now())) {
            holder.parentView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.parentView.setBackgroundColor(Color.WHITE);
        }

        if(date.equals(LocalDate.now())) {
            holder.parentView.setBackgroundColor(Color.YELLOW);
        }
        if(date.equals(CalendarUtils.selectedDate)) {
            holder.parentView.setBackgroundColor(Color.LTGRAY);
        }
        if (date.isAfter(LocalDate.now())) {
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
            holder.rating.setTextColor(Color.LTGRAY);
        } else if(date.getMonth().equals(CalendarUtils.selectedDate.getMonth())) {
            holder.dayOfMonth.setTextColor(Color.BLACK);
            holder.rating.setTextColor(Color.BLACK);
        } else {
            holder.dayOfMonth.setTextColor(Color.WHITE);
            holder.emotions.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.state_0_white));
            holder.rating.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface  OnItemListener {
        void onItemClick(int position, LocalDate date);
    }

}
