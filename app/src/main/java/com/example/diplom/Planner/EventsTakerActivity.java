package com.example.diplom.Planner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diplom.CalendarUtils;
import com.example.diplom.R;

import java.text.SimpleDateFormat;
public class EventsTakerActivity extends AppCompatActivity {

    private EditText eventTaskEditText;
    private TextView eventDateTextView;
    private Events events;
    private boolean isOldEvent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_taker);

        eventTaskEditText = findViewById(R.id.eventTaskEditText);
        eventDateTextView = findViewById(R.id.eventDateTextView);

        eventDateTextView.setText(CalendarUtils.formattedDateUa(CalendarUtils.selectedDate));

        Button eventSave = findViewById(R.id.eventSave);

        events = new Events();
        try {
            events = (Events) getIntent().getSerializableExtra("old_event");
            eventTaskEditText.setText(events.getTask());
            isOldEvent = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        eventSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = eventTaskEditText.getText().toString();

                if (task.isEmpty()) {
                    Toast.makeText(EventsTakerActivity.this, "Задача пуста", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");

                if (!isOldEvent) {
                    events = new Events();
                }

                events.setTask(task);
                events.setDate(CalendarUtils.formattedDate(CalendarUtils.selectedDate));

                Intent intent = new Intent();
                intent.putExtra("event", events);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

}
