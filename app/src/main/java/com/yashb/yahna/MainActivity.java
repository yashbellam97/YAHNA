package com.yashb.yahna;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TOP_ARTICLE_IDS_URL = "https://hacker-news.firebaseio.com/v0/topstories.json";
    int start = 0;
    int end = 20;
    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;
    boolean loading;
    private RecyclerView recyclerView;
    private ArticleAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ProgressBar homeProgressBar;
    private String[] topArticleIds;
    private ArrayList<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView internetTextview = (TextView) findViewById(R.id.internet_textview);
        ImageView internetImageView = (ImageView) findViewById(R.id.internet_imageview);
        homeProgressBar = findViewById(R.id.home_progressbar);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            startLoad();
        } else {
            internetTextview.setText("No Internet...");
            internetTextview.setVisibility(View.VISIBLE);
            internetImageView.setVisibility(View.VISIBLE);
            homeProgressBar.setVisibility(View.GONE);
        }
    }

    private void startLoad() {
        loading = true;
        recyclerView = findViewById(R.id.home_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        articleList = new ArrayList<>();
        mAdapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ArticleAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Article article = mAdapter.getItem(position);
                if (article != null) {
                    String articleUrl = article.getmSource();
                    if (!articleUrl.startsWith("http://") && !articleUrl.startsWith("https://")) {
                        articleUrl = "http://" + articleUrl;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to fetch URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TopArticleIdsAsyncTask task = new TopArticleIdsAsyncTask();
        task.execute(TOP_ARTICLE_IDS_URL);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            fetchMoreArticles(end);
                            loading = true;
                        }
                    }
                }
            }
        });
    }

    void fetchMoreArticles(int itemCount) {
        ArticleAsyncTask articleTask = new ArticleAsyncTask();
        end += 20; //Prevent redundant articles if some go missing
        articleTask.execute(itemCount, itemCount + 20);
    }

    private class TopArticleIdsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            return QueryUtils.getTopIDs(urls[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                s = s.substring(1, s.length() - 1);
                topArticleIds = s.split(",");
            }
            ArticleAsyncTask articleTask = new ArticleAsyncTask();
            articleTask.execute(start, end);
        }
    }

    private class ArticleAsyncTask extends AsyncTask<Integer, Void, List<Article>> {
        @Override
        protected List<Article> doInBackground(Integer... params) {

            if (topArticleIds.length < 1 || topArticleIds[0] == null) {
                return null;
            }
            return QueryUtils.getArticles(topArticleIds, params[0], params[1]);
        }

        @Override
        protected void onPostExecute(List<Article> newArticles) {
            if (newArticles != null) {
                articleList.addAll(newArticles);
                mAdapter.notifyDataSetChanged();
                loading = false;
                homeProgressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

}
