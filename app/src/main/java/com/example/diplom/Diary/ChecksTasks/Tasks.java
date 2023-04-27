package com.example.diplom.Diary.ChecksTasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tasks")
public class Tasks implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID;

    @ColumnInfo(name = "task")
    String task;

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
}
