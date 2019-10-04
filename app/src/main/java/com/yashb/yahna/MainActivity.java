package com.yashb.yahna;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TOP_ARTICLE_IDS_URL = "https://hacker-news.firebaseio.com/v0/topstories.json";
    private ListView listView;
    private ArticleAdapter mAdapter;
    private String[] topArticleIds;


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

        mAdapter = new ArticleAdapter(this, articleList);
        listView.setAdapter(mAdapter);

        TopArticleIdsAsyncTask task = new TopArticleIdsAsyncTask();
        task.execute(TOP_ARTICLE_IDS_URL);

    }

    private class TopArticleIdsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            String result = QueryUtils.getTopIDs(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                s = s.substring(1, s.length() - 1);
                topArticleIds = s.split(",");
            }
        }
    }

}
