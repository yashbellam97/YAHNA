package com.yashb.yahna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] articleNames = {"Article 1", "Article 2", "Article 3", "Article 4"};

        ArrayAdapter articleNameAdapter = new ArrayAdapter<>(this, R.layout.home_listview_item, articleNames);

        ListView listView = (ListView) findViewById(R.id.home_listview);
        listView.setAdapter(articleNameAdapter);
    }
}
