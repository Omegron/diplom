package com.example.diplom;

import androidx.cardview.widget.CardView;

import com.example.diplom.Models.Notes;

public interface NotesClickListener {

    void onClick (Notes notes);
    void onLongClick (Notes notes, CardView cardView);

}
