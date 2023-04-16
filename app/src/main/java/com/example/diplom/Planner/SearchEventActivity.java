package com.example.diplom.Planner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.widget.ListView;

import com.example.diplom.R;

import java.util.ArrayList;

public class SearchEventActivity extends AppCompatActivity{

    private ListView eventSearchListView;
    private SearchView searchEvent;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        initWidgets();

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
    }

    private void filter(String newText) {
        ArrayList<Event> filteredList = new ArrayList<>();
        for (Event event : Event.eventsList) {
            if (event.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(event);
            }
        }
        eventAdapter = new EventAdapter(getApplicationContext(), filteredList);
        eventSearchListView.setAdapter(eventAdapter);
    }

    private void initWidgets() {
        eventSearchListView = findViewById(R.id.eventSearchListView);
        searchEvent = findViewById(R.id.searchEvent);
    }

    private void setSearchView() {
        setEventAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSearchView();
    }

    private void setEventAdapter() {
        ArrayList<Event> events = Event.eventsList;
        events = Event.eventSorting(events);
        eventAdapter = new EventAdapter(getApplicationContext(), events);
        eventSearchListView.setAdapter(eventAdapter);
    }

}
