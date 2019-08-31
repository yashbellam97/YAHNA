package com.yashb.yahna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.home_listview);

        ArrayList<Article> articleList = new ArrayList<>();
        articleList.add(new Article("Apple devices hacked", "google.com", "Tim Apple", "56 points"));
        articleList.add(new Article("Dorian may turn into a Cat 5", "weather.com", "Dorian Grey", "78 points"));
        articleList.add(new Article("Oneplus music festival", "oneplus.com", "Rob Adams", "123 points"));
        articleList.add(new Article("UF suspends classes on tuesday", "ufl.edu", "UF", "536 points"));
        articleList.add(new Article("CamScanner has malware", "technews.com", "Mal Ware", "30 points"));
        articleList.add(new Article("Don't use Java", "oracle.com", "Sedgewick", "91 points"));

        articleAdapter = new ArticleAdapter(this, articleList);

        listView.setAdapter(articleAdapter);

    }
}
