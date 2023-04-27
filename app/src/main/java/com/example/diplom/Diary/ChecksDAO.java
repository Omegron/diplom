package com.example.diplom.Diary;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChecksDAO {

    @Insert(onConflict = REPLACE)
    void insert (Checks checks);

    @Query("SELECT * FROM checks ORDER BY id DESC")
    List<Checks> getAll();

    @Query("SELECT * FROM checks WHERE date = :date")
    List<Checks> getDay(String date);

    @Query("UPDATE checks SET task = :taskNew WHERE task = :taskOld")
    void updateTask(String taskOld, String taskNew);

    @Query("UPDATE checks SET state = :state WHERE ID = :id")
    void updateState(int id, boolean state);

    @Delete
    void delete(Checks checks);

    @Query("DELETE FROM checks WHERE task = :task")
    void deleteByTask(String task);

}
