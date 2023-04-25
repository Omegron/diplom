package com.example.diplom.Articles;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticlesDAO {

    @Insert(onConflict = REPLACE)
    void insert (Articles article);

    @Query("SELECT * FROM articles ORDER BY id DESC")
    List<Articles> getAll();

    @Query("UPDATE articles SET state = :state WHERE ID = :id")
    void stateChange (int id, String state);

    @Query("DELETE FROM articles")
    public void nukeTable();

}
