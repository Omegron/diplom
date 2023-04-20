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

    @Query("SELECT * FROM events WHERE date = :date")
    List<Events> getDay(String date);

    @Query("UPDATE events SET task = :task WHERE ID = :id")
    void updateTask (int id, String task);

    @Query("UPDATE events SET state = :state WHERE ID = :id")
    void updateState (int id, boolean state);

    @Delete
    void delete (Events events);

}
