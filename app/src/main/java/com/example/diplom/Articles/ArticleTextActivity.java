package com.example.diplom.Articles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diplom.R;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class ArticleTextActivity extends AppCompatActivity {
    private TextView text_content;
    private String[] array_articles;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_content);
        array_articles = getResources().getStringArray(R.array.articles_texts_array);
        text_content = findViewById(R.id.articleTextView);
        receiveIntent();

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            int id = intent.getIntExtra("id", 0);
            if (id == 2) {
                String test = "";
                try {
                    InputStream inputStream = getAssets().open("articles.txt");
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    test = new String(buffer);
                } catch (IOException e){
                    System.out.println("Error");
                }
                text_content.setText(test);
            } else {
                text_content.setText(array_articles[id - 1]);
            }
        }
    }
}
