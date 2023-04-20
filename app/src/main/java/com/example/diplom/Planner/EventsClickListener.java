package com.example.diplom.Planner;

import androidx.cardview.widget.CardView;

public interface EventsClickListener {

    void onClick (Events events);
    void onLongClick (Events events, CardView cardView);

}
