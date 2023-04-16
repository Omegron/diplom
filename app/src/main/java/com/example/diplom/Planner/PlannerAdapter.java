package com.example.diplom.Planner;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

class PlannerAdapter extends RecyclerView.Adapter<PlannerViewHolder> {
    private final Activity activity;
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public PlannerAdapter(Activity activity, ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.activity = activity;
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public PlannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) {//month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        } else { // week view
            layoutParams.height = (int) parent.getHeight();
        }
        return new PlannerViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannerViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        if(date.equals(LocalDate.now())) {
            holder.parentView.setBackgroundColor(Color.YELLOW);
        }
        if(date.equals(CalendarUtils.selectedDate)) {
            holder.parentView.setBackgroundColor(Color.LTGRAY);
        }

        if(date.getMonth().equals(CalendarUtils.selectedDate.getMonth())) {
            holder.dayOfMonth.setTextColor(Color.BLACK);
            System.out.println(activity.getLocalClassName());
        } else if (Objects.equals(activity.getLocalClassName(), "PlannerActivity")) {
            holder.dayOfMonth.setTextColor(Color.WHITE);
        } else {
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
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