package com.example.diplom.Planner;

import static com.example.diplom.CalendarUtils.daysInWeekArray;
import static com.example.diplom.CalendarUtils.monthYearFromDate;
import static com.example.diplom.CalendarUtils.selectedDate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeeklyViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener,
        PopupMenu.OnMenuItemClickListener {
    private TextView monthYearText;
    private Intent intent;
    private EventsListAdapter eventsListAdapter;
    private CalendarAdapter calendarAdapter;
    private RecyclerView calendarRecyclerView;
    private RecyclerView eventWeekListView;
    private ArrayList<LocalDate> days;
    private List<Events> events = new ArrayList<>();
    private List<Events> eventsTemp = new ArrayList<>();
    private EventsDB database;
    private Events selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        eventWeekListView = findViewById(R.id.eventWeekListView);
        Button newEvent = findViewById(R.id.newEvent);

        database = EventsDB.getInstance(this);

        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeeklyViewActivity.this, EventsTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        monthYearText = findViewById(R.id.monthYearTV);

        days = daysInWeekArray(CalendarUtils.selectedDate);
        for (LocalDate date : days) {
            eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(date));
            for (Events event : eventsTemp) {
                events.add(event);
            }
        }
        Events.eventsSorting(events);
        updateRecycler(events);
        setWeekView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Events new_events = (Events) data.getSerializableExtra("event");
                database.eventsDAO().insert(new_events);
                events.clear();
                events.addAll(database.eventsDAO().getAll());
                int positionCalendar = 0;
                for (int i = 0; i < days.size(); i++) {
                    if (new_events.getDate().equals(CalendarUtils.formattedDate(days.get(i)))) {
                        positionCalendar = i;
                        break;
                    }
                }
                calendarAdapter.notifyItemChanged(positionCalendar);
                eventsListAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Events new_events = (Events) data.getSerializableExtra("event");
                database.eventsDAO().updateTask(new_events.getID(), new_events.getTask());
                events.clear();
                events.addAll(database.eventsDAO().getAll());
                eventsListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        days = daysInWeekArray(CalendarUtils.selectedDate);
        calendarAdapter = new CalendarAdapter(this, days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }

    private void updateRecycler(List<Events> events) {
        eventWeekListView.setHasFixedSize(true);
        eventWeekListView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        eventsListAdapter = new EventsListAdapter(WeeklyViewActivity.this, events, eventsClickListener);
        eventWeekListView.setAdapter(eventsListAdapter);
    }

    private final EventsClickListener eventsClickListener = new EventsClickListener() {
        @Override
        public void onClick(Events events) {
            Intent intent = new Intent(WeeklyViewActivity.this, EventsTakerActivity.class);
            intent.putExtra("old_event", events);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Events events, CardView cardView) {
            selectedEvent = new Events();
            selectedEvent = events;
            showPopup (cardView);
        }

        @Override
        public void onCheckboxClick(Events event, CheckBox checkBox) {
            int positionCalendar = 0;
            for (int i = 0; i < days.size(); i++) {
                if (event.getDate().equals(CalendarUtils.formattedDate(days.get(i)))) {
                    positionCalendar = i;
                    break;
                }
            }
            int positionEvent = 0;
            for (int i = 0; i < events.size(); i++) {
                if (event.getID() == events.get(i).getID()) {
                    positionEvent = i;
                    break;
                }
            }
            event.setState(checkBox.isChecked());
            database.eventsDAO().updateState(event.getID(), event.getState());
            //calendarAdapter.notifyDataSetChanged();
            calendarAdapter.notifyItemChanged(positionCalendar);
            int finalPositionEvent = positionEvent;
            eventWeekListView.post(new Runnable()
            {
                @Override
                public void run() {
                    eventsListAdapter.notifyItemChanged(finalPositionEvent);
                    //eventsListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.event_popup_menu);
        popupMenu.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            database.eventsDAO().delete(selectedEvent);
            events.remove(selectedEvent);
            int positionCalendar = 0;
            for (int i = 0; i < days.size(); i++) {
                if (selectedEvent.getDate().equals(CalendarUtils.formattedDate(days.get(i)))) {
                    positionCalendar = i;
                    break;
                }
            }
            calendarAdapter.notifyItemChanged(positionCalendar);
            eventsListAdapter.notifyDataSetChanged();
            Toast.makeText(WeeklyViewActivity.this, "Event removed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }



    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        events.clear();
        days = daysInWeekArray(CalendarUtils.selectedDate);
        for (LocalDate date : days) {
            eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(date));
            for (Events event : eventsTemp) {
                events.add(event);
            }
        }
        Events.eventsSorting(events);
        setWeekView();
        updateRecycler(events);
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        events.clear();
        days = daysInWeekArray(CalendarUtils.selectedDate);
        for (LocalDate date : days) {
            eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(date));
            for (Events event : eventsTemp) {
                events.add(event);
            }
        }
        Events.eventsSorting(events);
        setWeekView();
        updateRecycler(events);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        events.clear();
        days = daysInWeekArray(CalendarUtils.selectedDate);
        for (LocalDate date : days) {
            eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(date));
            for (Events event : eventsTemp) {
                events.add(event);
            }
        }
        Events.eventsSorting(events);
        updateRecycler(events);
    }

    public void monthlyAction(View view) {
        intent = new Intent(this, PlannerActivity.class);
        intent.putExtra("month", selectedDate.toString());
        startActivity(intent);
    }

    public void dailyAction(View view) {
        intent = new Intent(this, DailyViewActivity.class);
        startActivity(intent);
    }

    public void searchAction(View view) {
        intent = new Intent(this, SearchEventActivity.class);
        startActivity(intent);
    }
}
