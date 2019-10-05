package com.yashb.yahna;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static String getTopIDs(String topIdsUrlString) {
        URL topIdsUrl = createUrl(topIdsUrlString);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpsRequest(topIdsUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTPS request", e);
        }
        return jsonResponse;
    }

    public static List<Article> getArticles(String[] articleIds) {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            String url = "https://hacker-news.firebaseio.com/v0/item/" + articleIds[i] + ".json";
            Article article = fetchArticle(url);
            if(article != null) {
                articles.add(article);
            }
        }
        return articles;
    }

    public static Article fetchArticle(String articleUrlString) {
        URL articleUrl = createUrl(articleUrlString);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpsRequest(articleUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTPS request", e);
        }

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to create JSON object", e);
        }

        return extractArticleFeatures(jsonObject);
    }

    private static Article extractArticleFeatures(JSONObject jsonObject) {
        Article article = null;
        try {
            String title = jsonObject.getString("title");
            String source = jsonObject.getString("url");
            String[] urlArray = source.split("/+");
            source = urlArray[1].replace("www.", "");

            String author = jsonObject.getString("by");
            String points = jsonObject.getString("score");

            article = new Article(title, source, author, points);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to parse JSON", e);
        }
        return article;
    }

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building URL", e);
        }
        return url;
    }

    private static String makeHttpsRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON result", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }


}

