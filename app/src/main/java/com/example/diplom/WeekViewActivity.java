package com.example.diplom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.diplom.CalendarUtils.daysInMonthArray;
import static com.example.diplom.CalendarUtils.daysInWeekArray;
import static com.example.diplom.CalendarUtils.monthYearFromDate;
import static com.example.diplom.CalendarUtils.selectedDate;

public class WeekViewActivity extends AppCompatActivity implements PlannerAdapter.OnItemListener {
    private TextView monthYearText;
    private Intent intent;
    private RecyclerView plannerRecyclerView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
    }

    private void initWidgets() {
        plannerRecyclerView = findViewById(R.id.plannerRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
        System.out.println(days.get(0));
        PlannerAdapter plannerAdapter = new PlannerAdapter(this, days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        plannerRecyclerView.setLayoutManager(layoutManager);
        plannerRecyclerView.setAdapter(plannerAdapter);
        setEventAdapter();
    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForWeek(selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view) {
        intent = new Intent(this, EventEditActivity.class);
        startActivity(intent);
    }

    public void dailyAction(View view) {
        intent = new Intent(this, DailyCalendarActivity.class);
        startActivity(intent);
    }

}
