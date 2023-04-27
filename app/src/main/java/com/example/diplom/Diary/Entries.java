package com.example.diplom.Diary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "entries")
public class Entries implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID;

    @ColumnInfo(name = "note")
    String note = "";
    @ColumnInfo(name = "emotion")
    String emotion;

    @ColumnInfo(name = "rating")
    String rating;

    @ColumnInfo(name = "date")
    String date;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
