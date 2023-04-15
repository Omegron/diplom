package com.example.diplom;

import static com.example.diplom.CalendarUtils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diplom.Models.Notes;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
