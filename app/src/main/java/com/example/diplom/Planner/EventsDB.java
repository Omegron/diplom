package com.example.diplom.Planner;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Events.class, version = 1, exportSchema = false)
public abstract class EventsDB extends RoomDatabase {

    private static EventsDB database;
    private static String DATABASE_NAME = "EventAct";

    public synchronized static EventsDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), EventsDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract EventsDAO eventsDAO();

}
