package com.example.diplom.Notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextNotes;
    private Button noteSave;
    private Notes notes;
    private boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextNotes = findViewById(R.id.editTextNotes);

        noteSave = findViewById(R.id.noteSave);

        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            editTextTitle.setText(notes.getTitle());
            editTextNotes.setText(notes.getNotes());
            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        noteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                String content = editTextNotes.getText().toString();

                if (content.isEmpty()) {
                    Toast.makeText(NotesTakerActivity.this, "Нотаток пустий", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm:ss");
                Date dateD = new Date();
                LocalDate dateL = LocalDate.now();

                if (!isOldNote) {
                    notes = new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(content);
                notes.setDate(CalendarUtils.formattedDateUa(dateL) + formatter.format(dateD));

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}