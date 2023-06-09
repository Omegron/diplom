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
        for (int i = 19; i > 14; i--) {
            articlesList1.add(articles.get(i));
        }

        List<Articles> articlesList2 = new ArrayList<>();
        for (int i = 14; i > 9; i--) {
            articlesList2.add(articles.get(i));
        }

        List<Articles> articlesList3 = new ArrayList<>();
        for (int i = 9; i > 4; i--) {
            articlesList3.add(articles.get(i));
        }

        List<Articles> articlesList4 = new ArrayList<>();
        for (int i = 4; i > -1; i--) {
            articlesList4.add(articles.get(i));
        }

        mList.add(new DataModel(articlesList1 , "Навчання"));
        mList.add(new DataModel(articlesList2,"Здоров'я"));
        mList.add(new DataModel(articlesList3,"Тайм-менеджмент"));
        mList.add(new DataModel(articlesList4 ,"Лідерство"));

        return mList;
    }
}
