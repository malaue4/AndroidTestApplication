package com.example.marti.smalltestapplication.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.marti.smalltestapplication.BR;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsFeed extends BaseObservable {
    private int Id;
    private String title;
    private String description;
    private Uri link;
    private Bitmap icon;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

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

    public Uri getLink() {
        return link;
    }

    public void setLink(Uri link) {
        this.link = link;
        fetchIcon();
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    @Bindable
    public Bitmap getIcon() {
        return icon;
    }


    public void fetchIcon() {
        AsyncTask.execute(()->{
            try {
                URL url = new URL(String.format("https://www.google.com/s2/favicons?domain=%s", link.getHost()));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                setIcon(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }});
    }
}
