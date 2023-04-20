package com.example.diplom.Planner;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity(tableName = "events")
public class Events implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID;

    @ColumnInfo(name = "task")
    String task;

    @ColumnInfo(name = "date")
    String date;

    @ColumnInfo(name = "state")
    boolean state;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String event) {
        this.task = event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    //////////////////////////////////////////////////////////

    public static List<Events> eventsSorting(List<Events> events) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        int lengthControl = events.size();
        for (int i = 0; i < events.size(); i++) {
            LocalDate max = LocalDate.parse(events.get(0).getDate(), formatter);
            int temp = 0;
            Events tempMaxEvent = events.get(0);
            for (int j = 0; j < lengthControl; j++){
                if (LocalDate.parse(events.get(j).getDate(), formatter).isAfter(max)) {
                    max = LocalDate.parse(events.get(j).getDate(), formatter);
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

}
