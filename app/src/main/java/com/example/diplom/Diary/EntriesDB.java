package com.example.diplom.Diary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Entries.class, version = 1, exportSchema = false)
public abstract class EntriesDB extends RoomDatabase {

    private static EntriesDB database;
    private static String DATABASE_NAME = "EntriesAct";

    public synchronized static EntriesDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), EntriesDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract EntriesDAO entriesDAO();
}
