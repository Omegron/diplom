package com.example.diplom.Articles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diplom.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ArticleTextActivity extends AppCompatActivity {
    private TextView text_content;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_content);

        text_content = findViewById(R.id.articleTextView);
        receiveIntent();

    }

    private void getArticle(InputStream articles, int id) throws IOException {
        Reader reader = new InputStreamReader(articles, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);

        StringBuilder sb = new StringBuilder();
        String line;
        int temp = 0;

        while (!Objects.equals(line = br.readLine(), null)) {
            if (temp == 1 && line.equals("end")) {
                temp = 0;
            } else if (temp == 1) {
                sb.append(line);
                sb.append("\n");
            } else if (Objects.equals(line, "<<" + id + ">>")) {
                temp = 1;
            }
        }
        br.close();
        text_content.setText(sb);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String[] array_names = this.getResources().getStringArray(R.array.articles_names_array);

        if (intent != null) {
            int id = intent.getIntExtra("id", 0);
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(array_names[id-1]);
            try {
                InputStream inputStream = getAssets().open("articles.txt");
                getArticle(inputStream, id);
            } catch (IOException e){
                System.out.println("Error");
            }
        }
    }
}
