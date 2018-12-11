package com.example.marti.smalltestapplication;

import android.net.Uri;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchRssTask extends AsyncTask<String, Void, List<Article>> {

    public FetchRssTask() {
    }

    @Override
    protected List<Article> doInBackground(String... url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url[0]).build();

        try{
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                RssParser parser = new RssParser();
                parser.parseXML(response.body().string());
                return parser.articles;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class RssParser {
        private NewsFeed newsFeed;
        private ArrayList<Article> articles;
        private Article currentArticle;

        public RssParser() {
            this.articles = new ArrayList<>();
            this.newsFeed = new NewsFeed();
            this.currentArticle = null;
        }

        public void parseXML(String xml) throws XmlPullParserException, IOException {
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
                                    currentArticle.setSource(newsFeed);
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

        }
    }
}
