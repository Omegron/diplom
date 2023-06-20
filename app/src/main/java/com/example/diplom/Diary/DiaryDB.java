package com.example.diplom.Diary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Entries.class, Tasks.class, Checks.class}, version = 1, exportSchema = false)
public abstract class DiaryDB extends RoomDatabase {

    private static DiaryDB database;
    private static String DATABASE_NAME = "DiaryAct";

    public synchronized static DiaryDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), DiaryDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract DiaryDAO diaryDAO();

}
