package com.example.diplom.Planner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.diplom.R;

import java.util.ArrayList;
import java.util.List;

public class SearchEventActivity extends AppCompatActivity{

    private RecyclerView eventSearchListView;
    private EventsListAdapter eventsListAdapter;
    private EventsClickListener eventsClickListener;
    private List<Events> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        eventSearchListView = findViewById(R.id.eventSearchListView);
        SearchView searchEvent = findViewById(R.id.searchEvent);
        EventsDB database = EventsDB.getInstance(this);
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

    private void updateRecycler(List<Events> events) {
        eventSearchListView.setHasFixedSize(true);
        eventSearchListView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        eventsListAdapter = new EventsListAdapter(SearchEventActivity.this, events, eventsClickListener);
        eventSearchListView.setAdapter(eventsListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecycler(Events.eventsSorting(events));
    }

}
