package com.example.diplom.Articles;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.diplom.Diary.DiaryActivity;
import com.example.diplom.Notes.NotesActivity;
import com.example.diplom.Planner.PlannerActivity;
import com.example.diplom.R;
import com.example.diplom.Settings.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ArticlesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    private RecyclerView itemRecyclerView;
    private final DataPush dataPush = new DataPush();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        dataPush.databaseDataPush(this);
        List<DataModel> mList = dataPush.dataModelsDataPush();

        ArticlesDB database = ArticlesDB.getInstance(this);

        recyclerView = findViewById(R.id.main_recyclerview);

        final LayoutInflater factory = getLayoutInflater();
        final View view = factory.inflate(R.layout.each_item, null);
        itemRecyclerView = view.findViewById(R.id.child_rv);

        updateRecycler(mList);
        updateRecyclerArticles(database.articlesDAO().getAll());

    }

    private void updateRecycler(List<DataModel> list) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        ItemAdapter itemAdapter = new ItemAdapter(ArticlesActivity.this, list);
        recyclerView.setAdapter(itemAdapter);
    }

    private void updateRecyclerArticles(List<Articles> list) {
        ArticlesAdapter articlesAdapter = new ArticlesAdapter(ArticlesActivity.this, list);
        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        itemRecyclerView.setAdapter(articlesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.articles, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent;
        switch (id) {
            case R.id.diary: intent = new Intent(this, DiaryActivity.class);
                startActivity(intent);
                break;
            case R.id.notes: intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                break;
            case R.id.planner: intent = new Intent(this, PlannerActivity.class);
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