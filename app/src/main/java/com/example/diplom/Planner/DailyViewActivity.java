package com.example.diplom.Planner;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DailyViewActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private Intent intent;
    private EventsListAdapter eventsListAdapter;
    private RecyclerView eventDaysListView;
    private List<Events> events = new ArrayList<>();
    private EventsDB database;
    private Events selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_view);

        eventDaysListView = findViewById(R.id.eventDaysListView);
        Button newEvent = findViewById(R.id.newEvent);

        database = EventsDB.getInstance(this);

        updateRecycler(events);

        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DailyViewActivity.this, EventsTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);

        events = database.eventsDAO().getDay(CalendarUtils.formattedDate(selectedDate));
        Events.eventsSorting(events);
        updateRecycler(events);

        setDayView();
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

    private void updateRecycler(List<Events> events) {
        eventDaysListView.setHasFixedSize(true);
        eventDaysListView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        eventsListAdapter = new EventsListAdapter(DailyViewActivity.this, events, eventsClickListener);
        eventDaysListView.setAdapter(eventsListAdapter);
    }

    private final EventsClickListener eventsClickListener = new EventsClickListener() {
        @Override
        public void onClick(Events events) {
            Intent intent = new Intent(DailyViewActivity.this, EventsTakerActivity.class);
            intent.putExtra("old_event", events);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Events events, CardView cardView) {
            selectedEvent = events;
            showPopup (cardView);
        }

        @Override
        public void onCheckboxClick(Events event, CheckBox checkBox) {
            event.setState(checkBox.isChecked());
            database.eventsDAO().updateState(event.getID(), event.getState());
            eventsListAdapter.notifyDataSetChanged();
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
            eventsListAdapter.notifyDataSetChanged();
            Toast.makeText(DailyViewActivity.this, "Event removed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        events = database.eventsDAO().getDay(CalendarUtils.formattedDate(selectedDate));
        Events.eventsSorting(events);
        setDayView();
        updateRecycler(events);
    }

    private void setDayView() {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        //setEventAdapter();
    }

    public void previousDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        events = database.eventsDAO().getDay(CalendarUtils.formattedDate(selectedDate));
        Events.eventsSorting(events);
        setDayView();
        updateRecycler(events);
    }

    public void nextDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        events = database.eventsDAO().getDay(CalendarUtils.formattedDate(selectedDate));
        Events.eventsSorting(events);
        setDayView();
        updateRecycler(events);
    }

    public void monthlyAction(View view) {
        intent = new Intent(this, PlannerActivity.class);
        intent.putExtra("month", selectedDate.toString());
        startActivity(intent);
    }

    public void weeklyAction(View view) {
        intent = new Intent(this, WeeklyViewActivity.class);
        startActivity(intent);
    }

    public void searchAction(View view) {
        intent = new Intent(this, SearchEventActivity.class);
        startActivity(intent);
    }
}
