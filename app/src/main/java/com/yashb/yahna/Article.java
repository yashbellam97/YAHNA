package com.yashb.yahna;

public class Article {

    private String mTitle;
    private String mSource;
    private String mAuthor;
    private String mPoints;

    public Article(String title, String source, String author, String points) {
        mTitle = title;
        mSource = source;
        mAuthor = author;
        mPoints = points;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
        this.mTitle = title;
    }

    public String getmSource() {
        return mSource;
    }

    public void setmSource(String source) {
        this.mSource = source;
    }

    public String getmPoints() {
        return mPoints;
    }

    public void setmPoints(String mPoints) {
        this.mPoints = mPoints;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String author) {
        this.mAuthor = author;
    }
}
