package com.example.diplom.Diary;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiaryDAO {

    //Entries
    @Insert(onConflict = REPLACE)
    void insertEntry (Entries entries);

    @Query("SELECT * FROM entries ORDER BY id DESC")
    List<Entries> getAllEntries();

    @Query("SELECT * FROM entries WHERE date = :date")
    Entries getDayE(String date);

    @Query("UPDATE entries SET emotion = :emotion WHERE ID = :id")
    void updateEmotion (int id, String emotion);

    @Query("UPDATE entries SET note = :note WHERE ID = :id")
    void updateNote (int id, String note);

    @Query("UPDATE entries SET rating = :rating WHERE ID = :id")
    void updateRating (int id, String rating);

    //Tasks
    @Insert(onConflict = REPLACE)
    void insertTask (Tasks tasks);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<Tasks> getAllTasks();

    @Query("UPDATE tasks SET task = :taskNew WHERE task = :taskOld")
    void updateTaskT(String taskOld, String taskNew);

    @Delete
    void deleteTask(Tasks tasks);

    @Query("DELETE FROM tasks WHERE task = :task")
    void deleteByTaskT(String task);

    //Checks
    @Insert(onConflict = REPLACE)
    void insertCheck (Checks checks);

    @Query("SELECT * FROM checks ORDER BY id DESC")
    List<Checks> getAllChecks();

    @Query("SELECT * FROM checks WHERE date = :date")
    List<Checks> getDayC(String date);

    @Query("UPDATE checks SET task = :taskNew WHERE task = :taskOld")
    void updateTaskC(String taskOld, String taskNew);

    @Query("UPDATE checks SET state = :state WHERE ID = :id")
    void updateState(int id, boolean state);

    @Delete
    void deleteCheck(Checks checks);

    @Query("DELETE FROM checks WHERE task = :task")
    void deleteByTaskC(String task);

}
