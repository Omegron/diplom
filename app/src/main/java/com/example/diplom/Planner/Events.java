package com.example.diplom.Planner;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(tableName = "events")
public class Events implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID;

    @ColumnInfo(name = "event")
    String event;

    @ColumnInfo(name = "date")
    String date;

    @ColumnInfo(name = "time")
    String time;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
