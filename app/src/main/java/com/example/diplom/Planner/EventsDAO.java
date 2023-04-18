package com.example.diplom.Planner;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventsDAO {

    @Insert(onConflict = REPLACE)
    void insert (Events events);

    @Query("SELECT * FROM events ORDER BY id DESC")
    List<Events> getAll();

    @Query("UPDATE events SET event = :event WHERE ID = :id")
    void update (int id, String event);

    @Delete
    void delete (Events events);

}
