package com.example.diplom.Diary;

import android.widget.CheckBox;

import androidx.cardview.widget.CardView;

public interface ChecksClickListener {

    void onLongClick (Checks check, CardView cardView);

    void onCheckboxClick (Checks check, CheckBox checkBox);

}
