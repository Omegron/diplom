package com.example.diplom.Planner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final Activity activity;
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(Activity activity, ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.activity = activity;
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) {//month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        } else { // week view
            layoutParams.height = (int) parent.getHeight();
        }
        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        final LocalDate date = days.get(position);

        EventsDB database = EventsDB.getInstance(activity);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));


        if(date.equals(CalendarUtils.selectedDate)) {
            holder.parentView.setBackgroundColor(Color.LTGRAY);
        } else if(date.equals(LocalDate.now())) {
            holder.parentView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.parentView.setBackgroundColor(Color.WHITE);
        }

        if(date.getMonth().equals(CalendarUtils.selectedDate.getMonth())) {
            List<Events> eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(date));
            if (!eventsTemp.isEmpty()) {
                int stateCount = 0;
                for (Events event : eventsTemp) {
                    if (event.getState()) {
                        stateCount++;
                    }
                }
                if (eventsTemp.size() == stateCount) {
                    holder.eventState.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.state_2));
                    System.out.println("date = " + date);
                } else {
                    holder.eventState.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.state_1));
                    System.out.println("date = " + date);
                }
            }
            holder.dayOfMonth.setTextColor(Color.BLACK);
        } else if (Objects.equals(activity.getLocalClassName(), "Planner.PlannerActivity")) {
            holder.dayOfMonth.setTextColor(Color.WHITE);
        } else {
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
        }

        for (LocalDate date1 : days) {
            if(date1.equals(CalendarUtils.selectedDate)) {
                //int color=((ColorDrawable)holder.parentView.getBackground()).getColor();
                System.out.println(((ColorDrawable)holder.parentView.getBackground()).getColor());
                //holder.parentView.setBackgroundColor(Color.LTGRAY);
            }
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