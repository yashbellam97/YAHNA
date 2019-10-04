package com.yashb.yahna;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private Context mContext;
    private List<Article> articleList;

    public ArticleAdapter(@NonNull Context context, List<Article> list) {
        super(context, 0, list);
        mContext = context;
        articleList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.home_listview_item, parent, false);
        }

        Article currentArticle = articleList.get(position);

        TextView articleTitle = (TextView) listItem.findViewById(R.id.article_title);
        articleTitle.setText(currentArticle.getmTitle());

        TextView source = (TextView) listItem.findViewById(R.id.source);
        source.setText(currentArticle.getmSource());

        TextView points = (TextView) listItem.findViewById(R.id.points);
        points.setText(currentArticle.getmPoints());

        TextView author = (TextView) listItem.findViewById(R.id.author);
        author.setText(currentArticle.getmAuthor());

        return listItem;
    }
}
