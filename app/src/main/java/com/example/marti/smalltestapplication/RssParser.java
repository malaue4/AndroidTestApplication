package com.example.marti.smalltestapplication;

import android.net.Uri;

import com.example.marti.smalltestapplication.model.Article;
import com.example.marti.smalltestapplication.model.NewsFeed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class RssParser {

    public static ArrayList<Article> parseXML(String xml) throws XmlPullParserException, IOException {
        NewsFeed newsFeed = new NewsFeed();
        ArrayList<Article> articles = new ArrayList<>();
        Article currentArticle = null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(false);

        XmlPullParser xmlPullParser = factory.newPullParser();

        xmlPullParser.setInput(new StringReader(xml));
        int event = xmlPullParser.getEventType();
        switch (event){
            case XmlPullParser.START_DOCUMENT:
            case XmlPullParser.START_TAG:
            case XmlPullParser.TEXT:
            case XmlPullParser.END_TAG:
            case XmlPullParser.END_DOCUMENT:
        }
        event = xmlPullParser.next();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG:
                    if(currentArticle == null){
                        switch (xmlPullParser.getName().toLowerCase()){
                            case "item":
                                currentArticle = new Article();
                                currentArticle.setNewsFeed(newsFeed);
                                break;
                            case "title":
                                newsFeed.setTitle(xmlPullParser.nextText());
                                break;
                            case "description":
                                newsFeed.setDescription(xmlPullParser.nextText());
                                break;
                            case "link":
                                newsFeed.setLink(Uri.parse(xmlPullParser.nextText()));
                                break;
                        }
                    } else {
                        switch (xmlPullParser.getName().toLowerCase()){
                            case "title":
                                currentArticle.setTitle(xmlPullParser.nextText());
                                break;
                            case "link":
                                currentArticle.setLink(Uri.parse(xmlPullParser.nextText()));
                                break;
                            case "dc:creator":
                                currentArticle.setAuthor(xmlPullParser.nextText());
                                break;
                            case "category":
                                currentArticle.setCategory(xmlPullParser.nextText());
                                break;
                            case "media:content":
                                //currentArticle.setThumbnail(xmlPullParser.getAttributeValue(null,"url"));
                                break;
                            case "description":
                                currentArticle.setDescription(xmlPullParser.nextText());
                                break;
                            case "pubdate":
                                currentArticle.setPublishDate(xmlPullParser.nextText());
                                break;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(xmlPullParser.getName().equalsIgnoreCase("item") && currentArticle != null) {
                        articles.add(currentArticle);
                        currentArticle = null;
                    }
                    break;
            }
            event = xmlPullParser.next();
        }

        return articles;

    }
}
