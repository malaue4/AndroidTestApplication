package com.example.marti.smalltestapplication.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsFeed {
    private int Id;
    private String Title;
    private String Description;
    private Uri Link;
    private Bitmap Icon;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

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

    public Uri getLink() {
        return Link;
    }

    public void setLink(Uri link) {
        Link = link;
        fetchIcon();
    }

    public void setIcon(Bitmap icon) {
        Icon = icon;
    }

    public Bitmap getIcon() {
        return Icon;
    }


    public void fetchIcon() {
        AsyncTask.execute(()->{
            try {
                URL url = new URL(String.format("https://www.google.com/s2/favicons?domain=%s", Link.getHost()));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                setIcon(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }});
    }
}
