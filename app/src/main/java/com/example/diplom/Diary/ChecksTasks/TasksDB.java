package com.example.diplom.Diary.ChecksTasks;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Tasks.class, version = 1, exportSchema = false)
public abstract class TasksDB extends RoomDatabase {

    private static TasksDB database;
    private static String DATABASE_NAME = "TasksAct";

    public synchronized static TasksDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), TasksDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract TasksDAO tasksDAO();
}
