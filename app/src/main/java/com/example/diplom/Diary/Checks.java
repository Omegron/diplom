package com.example.diplom.Diary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "checks")
public class Checks implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID;

    @ColumnInfo(name = "task")
    String task;

    @ColumnInfo(name = "state")
    boolean state;

    @ColumnInfo(name = "date")
    String date;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
