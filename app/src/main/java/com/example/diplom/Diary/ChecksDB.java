package com.example.diplom.Diary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Checks.class, version = 1, exportSchema = false)
public abstract class ChecksDB extends RoomDatabase {

    private static ChecksDB database;
    private static String DATABASE_NAME = "ChecksAct";

    public synchronized static ChecksDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), ChecksDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract ChecksDAO checksDAO();

}
