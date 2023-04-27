package com.example.diplom.Diary.ChecksTasks;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TasksDAO {

    @Insert(onConflict = REPLACE)
    void insert (Tasks tasks);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<Tasks> getAll();

    @Query("UPDATE tasks SET task = :taskNew WHERE task = :taskOld")
    void updateTask(String taskOld, String taskNew);

    @Delete
    void delete(Tasks tasks);

    @Query("DELETE FROM tasks WHERE task = :task")
    void deleteByTask(String task);

}
