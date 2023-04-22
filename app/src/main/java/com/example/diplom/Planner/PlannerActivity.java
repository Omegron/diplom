package com.example.diplom.Planner;

import static com.example.diplom.CalendarUtils.daysInMonthArray;
import static com.example.diplom.CalendarUtils.monthYearFromDate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.diplom.Articles.ArticlesActivity;
import com.example.diplom.CalendarUtils;
import com.example.diplom.Diary.DiaryActivity;
import com.example.diplom.Notes.NotesActivity;
import com.example.diplom.R;
import com.example.diplom.Settings.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class PlannerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CalendarAdapter.OnItemListener, PopupMenu.OnMenuItemClickListener {

    private DrawerLayout drawer;
    private Intent intent;
    private TextView monthYearText;
    private EventsListAdapter eventsListAdapter;
    private CalendarAdapter calendarAdapter;
    private RecyclerView calendarRecyclerView;
    private RecyclerView eventMonthListView;
    private ArrayList<LocalDate> daysInMonth;
    private List<Events> events = new ArrayList<>();
    private List<Events> eventsTemp = new ArrayList<>();
    private EventsDB database;
    private Events selectedEvent;
    private CheckBox eventCheckBox;
    private ImageView eventStateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        eventMonthListView = findViewById(R.id.eventMonthListView);

        database = EventsDB.getInstance(this);

        monthYearText = findViewById(R.id.monthYearTV);
        CalendarUtils.selectedDate = LocalDate.now();

        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("month").toString();
        if (!name.equals("First jump")) {
            CalendarUtils.selectedDate = LocalDate.parse(name);
        }

        daysInMonth = daysInMonthArray();
        System.out.println("//////////////////////////" + daysInMonth.get(0));
        for (int i = 0; i < daysInMonth.size(); i++) {
            Month month1 = daysInMonth.get(i).getMonth();
            Month month2 = daysInMonth.get(10).getMonth();
            if (month1.equals(month2)) {
                eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(daysInMonth.get(i)));
            }
            for (Events event : eventsTemp) {
                events.add(event);
            }
        }
        updateRecycler(events);

        setMonthView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        daysInMonth = daysInMonthArray();
        calendarAdapter = new CalendarAdapter(this, daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private void updateRecycler(List<Events> events) {
        eventMonthListView.setHasFixedSize(true);
        eventMonthListView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        eventsListAdapter = new EventsListAdapter(PlannerActivity.this, events, eventsClickListener);
        eventMonthListView.setAdapter(eventsListAdapter);
    }

    private final EventsClickListener eventsClickListener = new EventsClickListener() {
        @Override
        public void onClick(Events event) {
            Intent intent = new Intent(PlannerActivity.this, EventsTakerActivity.class);
            intent.putExtra("old_event", event);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Events event, CardView cardView) {
            selectedEvent = event;
            showPopup (cardView);
        }

        @Override
        public void onCheckboxClick(Events event, CheckBox checkBox) {
            int positionCalendar = 0;
            for (int i = 0; i < daysInMonth.size(); i++) {
                if (event.getDate().equals(CalendarUtils.formattedDate(daysInMonth.get(i)))) {
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
            //eventsListAdapter.notifyItemChanged(positionEvent);
            int finalPositionEvent = positionEvent;
            eventMonthListView.post(new Runnable()
            {
                @Override
                public void run() {
                    eventsListAdapter.notifyItemChanged(finalPositionEvent);
                    //eventsListAdapter.notifyDataSetChanged();
                }
            });
            //eventsListAdapter.notifyDataSetChanged();
            //eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(daysInMonth.get(i)));
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
            for (int i = 0; i < daysInMonth.size(); i++) {
                if (selectedEvent.getDate().equals(CalendarUtils.formattedDate(daysInMonth.get(i)))) {
                    positionCalendar = i;
                    break;
                }
            }
            calendarAdapter.notifyItemChanged(positionCalendar);
            eventsListAdapter.notifyDataSetChanged();
            Toast.makeText(PlannerActivity.this, "Event removed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        events.clear();
        eventsTemp.clear();
        daysInMonth = daysInMonthArray();
        for (int i = 0; i < daysInMonth.size(); i++) {
            Month month1 = daysInMonth.get(i).getMonth();
            Month month2 = daysInMonth.get(10).getMonth();
            if (month1.equals(month2)) {
                eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(daysInMonth.get(i)));
            }
            for (Events event : eventsTemp) {
                events.add(event);
            }
        }
        setMonthView();
        updateRecycler(events);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        events.clear();
        eventsTemp.clear();
        daysInMonth = daysInMonthArray();
        for (int i = 0; i < daysInMonth.size(); i++) {
            Month month1 = daysInMonth.get(i).getMonth();
            Month month2 = daysInMonth.get(10).getMonth();
            if (month1.equals(month2)) {
                eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(daysInMonth.get(i)));
            }
            for (Events event : eventsTemp) {
                events.add(event);
            }
        }

        setMonthView();
        updateRecycler(events);
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        events.clear();
        eventsTemp.clear();
        daysInMonth = daysInMonthArray();
        for (int i = 0; i < daysInMonth.size(); i++) {
            Month month1 = daysInMonth.get(i).getMonth();
            Month month2 = daysInMonth.get(10).getMonth();
            if (month1.equals(month2)) {
                eventsTemp = database.eventsDAO().getDay(CalendarUtils.formattedDate(daysInMonth.get(i)));
            }
            for (Events event : eventsTemp) {
                events.add(event);
            }
        }
        setMonthView();
        updateRecycler(events);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null && date.getMonth().equals(CalendarUtils.selectedDate.getMonth())) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    public void weeklyAction(View view) {
        intent = new Intent(this, WeeklyViewActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.planner, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent;
        switch (id) {
            case R.id.articles: intent = new Intent(this, ArticlesActivity.class);
                startActivity(intent);
                break;
            case R.id.diary: intent = new Intent(this, DiaryActivity.class);
                startActivity(intent);
                break;
            case R.id.notes: intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                break;
            case R.id.settings: intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}