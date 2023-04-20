package com.example.diplom.Planner;

import android.widget.CheckBox;

import androidx.cardview.widget.CardView;

public interface EventsClickListener {

    void onClick (Events events);
    void onLongClick (Events events, CardView cardView);

    void onCheckboxClick (Events events, CheckBox checkBox);

}
