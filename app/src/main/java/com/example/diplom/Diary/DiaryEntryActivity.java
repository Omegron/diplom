package com.example.diplom.Diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

public class DiaryEntryActivity extends AppCompatActivity {

    private ImageView emotion_choice, emotion_0, emotion_1, emotion_2, emotion_3,
            emotion_4, emotion_5, emotion_6, emotion_7;
    private EditText entryNote;
    private Entries entries;
    private String emotion = "0";
    private String rating = "0.00";
    private boolean isOldEntry = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);

        emotion_choice = findViewById(R.id.emotion_choice);
        emotion_0 = findViewById(R.id.emotion_0);
        emotion_1 = findViewById(R.id.emotion_1);
        emotion_2 = findViewById(R.id.emotion_2);
        emotion_3 = findViewById(R.id.emotion_3);
        emotion_4 = findViewById(R.id.emotion_4);
        emotion_5 = findViewById(R.id.emotion_5);
        emotion_6 = findViewById(R.id.emotion_6);
        emotion_7 = findViewById(R.id.emotion_7);

        Button emotionSave = findViewById(R.id.emotionSave);
        entryNote = findViewById(R.id.entryNote);

        entries = new Entries();
        try {
            entries = (Entries) getIntent().getSerializableExtra("old_entry");
            if (entries.getEmotion().equals("0")) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mood_0));
            } else if (entries.getEmotion().equals("1")) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mood_1));
            } else if (entries.getEmotion().equals("2")) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mood_2));
            } else if (entries.getEmotion().equals("3")) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mood_3));
            } else if (entries.getEmotion().equals("4")) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mood_4));
            } else if (entries.getEmotion().equals("5")) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mood_5));
            } else if (entries.getEmotion().equals("6")) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mood_6));
            } else if (entries.getEmotion().equals("7")) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mood_7));
            }
            emotion = entries.getEmotion();
            rating = entries.getRating();
            entryNote.setText(entries.getNote());
            isOldEntry = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        emotion_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(DiaryEntryActivity.this, R.drawable.mood_0));
                Toast.makeText(DiaryEntryActivity.this, "Choose " + emotion, Toast.LENGTH_SHORT).show();
                emotion = "0";
            }
        });

        emotion_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(DiaryEntryActivity.this, R.drawable.mood_1));
                Toast.makeText(DiaryEntryActivity.this, "Choose " + emotion, Toast.LENGTH_SHORT).show();
                emotion = "1";
            }
        });

        emotion_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(DiaryEntryActivity.this, R.drawable.mood_2));
                Toast.makeText(DiaryEntryActivity.this, "Choose " + emotion, Toast.LENGTH_SHORT).show();
                emotion = "2";
            }
        });

        emotion_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(DiaryEntryActivity.this, R.drawable.mood_3));
                Toast.makeText(DiaryEntryActivity.this, "Choose " + emotion, Toast.LENGTH_SHORT).show();
                emotion = "3";
            }
        });

        emotion_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(DiaryEntryActivity.this, R.drawable.mood_4));
                Toast.makeText(DiaryEntryActivity.this, "Choose " + emotion, Toast.LENGTH_SHORT).show();
                emotion = "4";
            }
        });

        emotion_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(DiaryEntryActivity.this, R.drawable.mood_5));
                Toast.makeText(DiaryEntryActivity.this, "Choose " + emotion, Toast.LENGTH_SHORT).show();
                emotion = "5";
            }
        });

        emotion_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(DiaryEntryActivity.this, R.drawable.mood_6));
                Toast.makeText(DiaryEntryActivity.this, "Choose " + emotion, Toast.LENGTH_SHORT).show();
                emotion = "6";
            }
        });

        emotion_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_choice.setImageDrawable(AppCompatResources.getDrawable(DiaryEntryActivity.this, R.drawable.mood_7));
                Toast.makeText(DiaryEntryActivity.this, "Choose " + emotion, Toast.LENGTH_SHORT).show();
                emotion = "7";
            }
        });

        emotionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String note = entryNote.getText().toString();

                if (!isOldEntry) {
                    entries = new Entries();
                }

                entries.setNote(note);
                entries.setRating(rating);
                entries.setEmotion(emotion);
                entries.setDate(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
                Intent intent = new Intent();
                intent.putExtra("entry", entries);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

}
