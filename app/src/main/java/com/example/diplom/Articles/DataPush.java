package com.example.diplom.Articles;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diplom.R;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPush extends AppCompatActivity {

    private final List<DataModel> mList = new ArrayList<>();
    private List<Articles> articles = new ArrayList<>();
    private ArticlesDB database;

    void databaseDataPush(Context context) {
        database = ArticlesDB.getInstance(context);

        String[] array_names = context.getResources().getStringArray(R.array.articles_names_array);

        articles = database.articlesDAO().getAll();
        int difference = array_names.length - articles.size();
        if (difference > 0) {
            for (int i = array_names.length - difference; i < array_names.length; i++) {
                Articles newArticle = new Articles();
                newArticle.setTitle(array_names[i]);
                newArticle.setState("0");
                database.articlesDAO().insert(newArticle);
            }
        }
    }

    public List<DataModel> dataModelsDataPush () {

        articles = database.articlesDAO().getAll();

        List<Articles> articlesList1 = new ArrayList<>();
        for (int i = 20; i > 15; i--) {
            articlesList1.add(articles.get(i));
        }

        List<Articles> articlesList2 = new ArrayList<>();
        for (int i = 15; i > 10; i--) {
            articlesList2.add(articles.get(i));
        }

        List<Articles> articlesList3 = new ArrayList<>();
        for (int i = 10; i > 5; i--) {
            articlesList3.add(articles.get(i));
        }

        List<Articles> articlesList4 = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            articlesList4.add(articles.get(i));
        }

        mList.add(new DataModel(articlesList1 , "Тема 1"));
        mList.add(new DataModel(articlesList2,"Тема 2"));
        mList.add(new DataModel(articlesList3,"Тема 3"));
        mList.add(new DataModel(articlesList4 ,"Тема 4"));

        return mList;
    }
}
