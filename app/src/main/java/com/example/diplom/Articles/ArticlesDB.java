package com.example.diplom.Articles;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Articles.class, version = 1, exportSchema = false)
public abstract class ArticlesDB extends RoomDatabase {

    private static ArticlesDB database;

    public synchronized static ArticlesDB getInstance(Context context) {
        if (database == null) {
            String DATABASE_NAME = "ArticlesAct";
            database = Room.databaseBuilder(context.getApplicationContext(), ArticlesDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract ArticlesDAO articlesDAO();
}
