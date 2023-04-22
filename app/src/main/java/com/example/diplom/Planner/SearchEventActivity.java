package com.example.diplom.Planner;

import static com.example.diplom.CalendarUtils.daysInMonthArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class SearchEventActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private RecyclerView eventSearchListView;
    private EventsListAdapter eventsListAdapter;
    private List<Events> events = new ArrayList<>();
    private Events selectedEvent;
    private EventsDB database;
    private String newTextResume = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        eventSearchListView = findViewById(R.id.eventSearchListView);
        SearchView searchEvent = findViewById(R.id.searchEvent);
        database = EventsDB.getInstance(this);
        events = database.eventsDAO().getAll();
        updateRecycler(events);
        searchEvent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter (newText);
                newTextResume = newText;
                return false;

            }
        });
        updateRecycler(Events.eventsSorting(events));
    }

    private void filter(String newText) {
        List<Events> filteredList = new ArrayList<>();
        for (Events event : events) {
            if (event.getTask().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(event);
            }
        }
        eventsListAdapter.filterList(filteredList);
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

    private void updateRecycler(List<Events> events) {
        eventSearchListView.setHasFixedSize(true);
        eventSearchListView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        eventsListAdapter = new EventsListAdapter(SearchEventActivity.this, events, eventsClickListener);
        eventSearchListView.setAdapter(eventsListAdapter);
    }

    private final EventsClickListener eventsClickListener = new EventsClickListener() {
        @Override
        public void onClick(Events event) {
            Intent intent = new Intent(SearchEventActivity.this, EventsTakerActivity.class);
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
            int positionEvent = 0;
            for (int i = 0; i < events.size(); i++) {
                if (event.getID() == events.get(i).getID()) {
                    positionEvent = i;
                    break;
                }
            }
            event.setState(checkBox.isChecked());
            database.eventsDAO().updateState(event.getID(), event.getState());
            int finalPositionEvent = positionEvent;
            eventSearchListView.post(new Runnable()
            {
                @Override
                public void run() {
                    eventsListAdapter.notifyItemChanged(finalPositionEvent);
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
            filter (newTextResume);
            eventsListAdapter.notifyDataSetChanged();
            Toast.makeText(SearchEventActivity.this, "Event removed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateRecycler(Events.eventsSorting(events));
    }

}
