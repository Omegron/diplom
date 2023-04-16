package com.example.diplom.Notes;

import androidx.cardview.widget.CardView;

import com.example.diplom.Notes.Notes;

public interface NotesClickListener {

    void onClick (Notes notes);
    void onLongClick (Notes notes, CardView cardView);

}
