package com.example.diplom;

import android.sax.EndElementListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList) {
            if(event.getDate().equals(date)) {
                events.add(event);
            }
        }
        return events;
    }

    public static ArrayList<Event> eventsForWeek(LocalDate days) {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList) {
            events.add(event);
        }
        int lengthControl = events.size();
        for (int i = 0; i < events.size(); i++) {
            LocalDate max = events.get(0).getDate();
            boolean after = true;
            int temp = 0;
            Event tempMaxEvent = events.get(0);
            for (int j = 0; j < lengthControl; j++){
                if (after == events.get(j).getDate().isAfter(max)) {
                    max = events.get(j).getDate();
                    tempMaxEvent = events.get(j);
                    temp = j;
                }
                if (j + 1 == lengthControl) {
                    events.set(temp, events.get(j));
                    events.set(j, tempMaxEvent);
                    lengthControl = lengthControl - 1;
                }
            }
        }
        return events;
    }

    private String name;
    private LocalDate date;
    private LocalTime time;

    public Event(String name, LocalDate date, LocalTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
