package com.example.diplom.Diary;

import static com.example.diplom.CalendarUtils.daysInMonthArray;
import static com.example.diplom.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.example.diplom.Articles.ArticlesActivity;
import com.example.diplom.CalendarUtils;
import com.example.diplom.Notes.NotesActivity;
import com.example.diplom.Planner.PlannerActivity;
import com.example.diplom.R;
import com.example.diplom.Settings.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class DiaryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DiaryAdapter.OnItemListener{

    private DrawerLayout drawer;
    private Intent intent;
    private TextView monthYearText;
    private RecyclerView diaryRecyclerView;
    private ArrayList<LocalDate> daysInMonth;

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

        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets() {
        diaryRecyclerView = findViewById(R.id.diaryRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        daysInMonth = daysInMonthArray();
        DiaryAdapter diaryAdapter = new DiaryAdapter(this, daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        diaryRecyclerView.setLayoutManager(layoutManager);
        diaryRecyclerView.setAdapter(diaryAdapter);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        if (!CalendarUtils.selectedDate.plusMonths(1).isAfter(LocalDate.now())) {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
            setMonthView();
        }
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null && date.getMonth().equals(CalendarUtils.selectedDate.getMonth()) && !date.isAfter(LocalDate.now())) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    public void emotionsAction() {

    }

    public void ratingAction() {

    }

    public void diaryEntryAction(View view) {
        intent = new Intent(this, DiaryEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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