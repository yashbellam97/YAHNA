package com.yashb.yahna;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private static ClickListener clickListener;
    private List<Article> articleList;

    ArticleAdapter(List<Article> list) {
        articleList = list;
    }

    void setOnItemClickListener(ClickListener clickListener) {
        ArticleAdapter.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listview_item, parent, false);
        return new ArticleViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        View listItem = holder.view;
        Article currentArticle = articleList.get(position);

        TextView articleTitle = (TextView) listItem.findViewById(R.id.article_title);
        articleTitle.setText(currentArticle.getmTitle());

        TextView source = (TextView) listItem.findViewById(R.id.source);
        String sourceUrl = currentArticle.getmSource();
        String[] urlArray = sourceUrl.split("/+");
        if (urlArray.length > 1) {
            sourceUrl = urlArray[1];
        }
        sourceUrl = sourceUrl.replace("www.", "");
        source.setText(sourceUrl);

        TextView points = (TextView) listItem.findViewById(R.id.points);
        points.setText(String.format("%s points", currentArticle.getmPoints()));

        TextView author = (TextView) listItem.findViewById(R.id.author);
        author.setText(currentArticle.getmAuthor());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    Article getItem(int position) {
        return articleList.get(position);
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;

        ArticleViewHolder(View v) {
            super(v);
            view = v;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getLayoutPosition(), view);
        }
    }

}
