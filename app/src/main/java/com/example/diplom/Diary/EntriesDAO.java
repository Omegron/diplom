package com.example.diplom.Diary;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EntriesDAO {

    @Insert(onConflict = REPLACE)
    void insert (Entries entries);

    @Query("SELECT * FROM entries ORDER BY id DESC")
    List<Entries> getAll();

    @Query("SELECT * FROM entries WHERE date = :date")
    Entries getDay(String date);

    @Query("UPDATE entries SET emotion = :emotion WHERE ID = :id")
    void updateEmotion (int id, String emotion);

    @Query("UPDATE entries SET note = :note WHERE ID = :id")
    void updateNote (int id, String note);

    @Query("UPDATE entries SET rating = :rating WHERE ID = :id")
    void updateRating (int id, String rating);

}
