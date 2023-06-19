package com.example.diplom.Diary;

import static com.example.diplom.CalendarUtils.daysInMonthArray;
import static com.example.diplom.CalendarUtils.formattedDateReverse;
import static com.example.diplom.CalendarUtils.monthYearFromDate;
import static com.example.diplom.CalendarUtils.selectedDate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.diplom.Articles.ArticlesActivity;
import com.example.diplom.CalendarUtils;
import com.example.diplom.Notes.NotesActivity;
import com.example.diplom.Planner.PlannerActivity;
import com.example.diplom.R;
import com.example.diplom.Settings.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DiaryAdapter.OnItemListener, PopupMenu.OnMenuItemClickListener {

    private DrawerLayout drawer;
    private Intent intent;
    private TextView monthYearText;
    private EditText checkWrite;
    private LinearLayout checkWriteTask;
    private RecyclerView diaryRecyclerView;
    private RecyclerView checksRecyclerView;
    private ArrayList<LocalDate> daysInMonth;
    private List<Entries> entries;
    private Entries entry;
    private List<Checks> checks = new ArrayList<>();
    private ChecksDB databaseC;
    private DiaryAdapter diaryAdapter;
    private ChecksListAdapter checksListAdapter;
    private Checks selectedCheck;
    private ChecksDataPush checksDataPush = new ChecksDataPush();
    private EntriesDB databaseE;
    private int cellView = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab_add = findViewById(R.id.fab_add);

        databaseE = EntriesDB.getInstance(this);
        entries = databaseE.entriesDAO().getAll();

        CalendarUtils.selectedDate = LocalDate.now();

        databaseC = ChecksDB.getInstance(this);
        checksDataPush.tasksDBDataPush(this);
        checksDataPush.checksForDayPush(this, CalendarUtils.selectedDate);
        checks = databaseC.checksDAO().getDay(CalendarUtils.formattedDate(CalendarUtils.selectedDate));

        initWidgets();

        setMonthView(cellView);
        updateRecycler(checks);

        checkWriteTask = findViewById(R.id.checkWriteTask);
        checkWrite = findViewById(R.id.checkWrite);
        Button checkSave = findViewById(R.id.checkSave);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaryRecyclerView.setVisibility(View.GONE);
                checkWriteTask.setVisibility(View.VISIBLE);
                checkSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String task = checkWrite.getText().toString();

                        if (task.isEmpty()) {
                            Toast.makeText(DiaryActivity.this, "Задача пуста", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        checksDataPush.insertChecksForDay(task, CalendarUtils.selectedDate);
                        Intent intent = new Intent(DiaryActivity.this, DiaryActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    private void initWidgets() {
        diaryRecyclerView = findViewById(R.id.diaryRecyclerView);
        checksRecyclerView = findViewById(R.id.checksRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Entries new_entries = (Entries) data.getSerializableExtra("entry");
                databaseE.entriesDAO().insert(new_entries);
                int positionCalendar = 0;
                for (int i = 0; i < daysInMonth.size(); i++) {
                    if (new_entries.getDate().equals(CalendarUtils.formattedDate(daysInMonth.get(i)))) {
                        positionCalendar = i;
                        break;
                    }
                }
                diaryAdapter.notifyItemChanged(positionCalendar);
            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Entries new_entries = (Entries) data.getSerializableExtra("entry");
                databaseE.entriesDAO().updateEmotion(new_entries.getID(), new_entries.getEmotion());
                databaseE.entriesDAO().updateNote(new_entries.getID(), new_entries.getNote());

                int positionCalendar = 0;
                for (int i = 0; i < daysInMonth.size(); i++) {
                    if (new_entries.getDate().equals(CalendarUtils.formattedDate(daysInMonth.get(i)))) {
                        positionCalendar = i;
                        break;
                    }
                }
                diaryAdapter.notifyItemChanged(positionCalendar);
            }
        }
    }

    private void setMonthView(int cellView) {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        daysInMonth = daysInMonthArray();
        diaryAdapter = new DiaryAdapter(this, daysInMonth, this, cellView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        diaryRecyclerView.setLayoutManager(layoutManager);
        diaryRecyclerView.setAdapter(diaryAdapter);
    }

    private void updateRecycler(List<Checks> checks) {
        checksRecyclerView.setHasFixedSize(true);
        checksRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        checksListAdapter = new ChecksListAdapter(DiaryActivity.this, checks, checksClickListener);
        checksRecyclerView.setAdapter(checksListAdapter);
    }

    private final ChecksClickListener checksClickListener = new ChecksClickListener() {

        @Override
        public void onLongClick(Checks check, CardView cardView) {
            selectedCheck = check;
            showPopup (cardView);
        }

        @Override
        public void onCheckboxClick(Checks check, CheckBox checkBox) {
            int positionEvent = 0;
            for (int i = 0; i < checks.size(); i++) {
                if (check.getID() == checks.get(i).getID()) {
                    positionEvent = i;
                    break;
                }
            }
            check.setState(checkBox.isChecked());
            databaseC.checksDAO().updateState(check.getID(), check.getState());
            int checkCount = 0;
            double rating;
            for (Checks checkC : checks) {
                if (checkC.getState()) {
                    checkCount = checkCount + 1;
                }
            }
            rating = (10.0 / checks.size()) * checkCount;
            String rating_2 = new DecimalFormat("#0.00").format(rating);
            entry = databaseE.entriesDAO().getDay(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
            if (entry != null) {
                databaseE.entriesDAO().updateRating(entry.getID(), rating_2);
            } else {
                entry = new Entries();
                entry.setNote("");
                entry.setEmotion("0");
                entry.setRating(rating_2);
                entry.setDate(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
                databaseE.entriesDAO().insert(entry);
            }
            int positionCalendar = 0;
            for (int i = 0; i < daysInMonth.size(); i++) {
                if (entry.getDate().equals(CalendarUtils.formattedDate(daysInMonth.get(i)))) {
                    positionCalendar = i;
                    break;
                }
            }
            diaryAdapter.notifyItemChanged(positionCalendar);
            int finalPositionEvent = positionEvent;
            checksRecyclerView.post(new Runnable()
            {
                @Override
                public void run() {
                    checksListAdapter.notifyItemChanged(finalPositionEvent);
                }
            });
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.check_popup_menu);
        popupMenu.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            checksDataPush.deleteChecksForDay(selectedCheck.getTask());
            checks.remove(selectedCheck);
            Toast.makeText(DiaryActivity.this, "Event removed", Toast.LENGTH_SHORT).show();
            updateRecycler(checks);
            for (Entries entriesC : entries) {
                CalendarUtils.selectedDate = formattedDateReverse(entriesC.getDate());
                checks = databaseC.checksDAO().getDay(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
                int checkCount = 0;
                double rating;
                for (Checks checkC : checks) {
                    if (checkC.getState()) {
                        checkCount = checkCount + 1;
                    }
                }
                rating = (10.0 / checks.size()) * checkCount;
                String rating_2 = new DecimalFormat("#0.00").format(rating);
                entry = databaseE.entriesDAO().getDay(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
                entry.setRating(rating_2);
                databaseE.entriesDAO().updateRating(entry.getID(), rating_2);
            }
            diaryAdapter.notifyDataSetChanged();
            return true;
        }

        if (item.getItemId() == R.id.update) {
            Button checkSave = findViewById(R.id.checkSave);
            diaryRecyclerView.setVisibility(View.GONE);
            checkWriteTask.setVisibility(View.VISIBLE);
            checkWrite.setText(selectedCheck.getTask());
            checkSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String task = checkWrite.getText().toString();

                    if (task.isEmpty()) {
                        Toast.makeText(DiaryActivity.this, "Задача пуста", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    checksDataPush.updateChecksForDay(selectedCheck.getTask(), task);
                    Intent intent = new Intent(DiaryActivity.this, DiaryActivity.class);
                    startActivity(intent);
                    Toast.makeText(DiaryActivity.this, "Event update", Toast.LENGTH_SHORT).show();
                }
            });
            updateRecycler(checks);
            return true;
        }
        return false;
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView(cellView);
        updateRecycler(checks);
    }

    public void nextMonthAction(View view) {
        if (!CalendarUtils.selectedDate.plusMonths(1).isAfter(LocalDate.now())) {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
            setMonthView(cellView);
            updateRecycler(checks);
        } else {
            CalendarUtils.selectedDate = LocalDate.now();
            setMonthView(cellView);
            updateRecycler(checks);
        }
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null && date.getMonth().equals(CalendarUtils.selectedDate.getMonth()) && !date.isAfter(LocalDate.now())) {
            CalendarUtils.selectedDate = date;
            checksDataPush.tasksDBDataPush(this);
            checksDataPush.checksForDayPush(this, CalendarUtils.selectedDate);
            checks = databaseC.checksDAO().getDay(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
            setMonthView(cellView);
            updateRecycler(checks);
        }
    }

    public void emotionsAction(View view) {
        if (!(cellView == 1)) {
            cellView = 1;
        } else {
            cellView = 0;
        }
        setMonthView(cellView);
    }

    public void ratingAction(View view) {
        if (!(cellView == 2)) {
            cellView = 2;
        } else {
            cellView = 0;
        }
        setMonthView(cellView);
    }

    public void diaryEntryAction(View view) {
        Intent intent = new Intent(DiaryActivity.this, DiaryEntryActivity.class);
        boolean isEntry = false;
        if (!entries.isEmpty()) {
            Entries entry = databaseE.entriesDAO().getDay(CalendarUtils.formattedDate(selectedDate));
            if (entry != null) {
                isEntry = true;
                intent.putExtra("old_entry", entry);
                startActivityForResult(intent, 102);
            } else {
                startActivityForResult(intent, 101);
            }
        } else {
            startActivityForResult(intent, 101);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diary, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent;
        switch (id) {
            case R.id.articles: intent = new Intent(this, ArticlesActivity.class);
                startActivity(intent);
                break;
            case R.id.planner: intent = new Intent(this, PlannerActivity.class);
                intent.putExtra("month", "First jump");
                startActivity(intent);
                break;
            case R.id.notes: intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                break;
            case R.id.settings: intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}