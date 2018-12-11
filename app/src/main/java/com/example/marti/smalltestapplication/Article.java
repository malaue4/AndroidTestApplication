package com.example.marti.smalltestapplication;

import android.net.Uri;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Article {
    public String Title;
    public String Description;
    public LocalDate PublishDate;
    public Uri Link;
    public NewsFeed Source;
    public boolean isFavorite;
    private String author;
    private String category;
    private String content;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public LocalDate getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        Date date = new Date(publishDate);
        setPublishDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
    public void setPublishDate(LocalDate publishDate) {
        PublishDate = publishDate;
    }

    public Uri getLink() {
        return Link;
    }

    public void setLink(Uri link) {
        Link = link;
    }

    public NewsFeed getSource() {
        return Source;
    }

    public void setSource(NewsFeed source) {
        Source = source;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}


