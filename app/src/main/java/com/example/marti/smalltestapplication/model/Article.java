package com.example.marti.smalltestapplication.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;
import com.example.marti.smalltestapplication.BR;

public class Article extends BaseObservable {
    private String title;
    private String description;
    private LocalDate publishDate;
    private Uri link;
    private NewsFeed newsFeed;
    private boolean favorite;
    private String author;
    private String category;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        DateTimeFormatter dtf = DateTimeFormatter.RFC_1123_DATE_TIME;
        try {
            setPublishDate(LocalDateTime.parse(publishDate, dtf).toLocalDate());
        } catch (DateTimeParseException e){
            setPublishDate(LocalDate.now());
        }
    }
    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Uri getLink() {
        return link;
    }

    public void setLink(Uri link) {
        this.link = link;
    }

    public NewsFeed getNewsFeed() {
        return newsFeed;
    }

    public void setNewsFeed(NewsFeed newsFeed) {
        this.newsFeed = newsFeed;
    }

    @Bindable
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        if(this.favorite != favorite) {
            this.favorite = favorite;
            notifyPropertyChanged(BR.favorite);
        }
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


