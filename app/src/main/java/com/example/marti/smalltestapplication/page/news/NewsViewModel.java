package com.example.marti.smalltestapplication.page.news;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.marti.smalltestapplication.RssParser;
import com.example.marti.smalltestapplication.model.Article;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsViewModel extends ViewModel {
    private MutableLiveData<List<Article>> articles;
    public LiveData<List<Article>> getArticles() {
        if(articles == null){
            articles = new MutableLiveData<>();

            //TODO: What happens if the feeds aren't ready? Do we observe? Will that catch the change when the value is posted before the MutableLiveData is returned to us?
            // List<String> feeds = getFeedUrls().getValue();
            List<String> feeds = new ArrayList<String>();
            //feeds.add("https://visualstudiomagazine.com/rss-feeds/columns.aspx");
            //feeds.add("https://www.xkcd.com/rss.xml");
            feeds.add("http://feeds.feedburner.com/d0od?format=xml");
            new FetchRssTask(this).execute(feeds.toArray(new String[0]));
        }
        return articles;
    }

    private MutableLiveData<List<String>> feedUrls;
    public LiveData<List<String>> getFeedUrls() {
        if(feedUrls == null){
            feedUrls = new MutableLiveData<>();
            //TODO: Fetch these from user data
            ArrayList<String> list = new ArrayList<String>();
            list.add("https://visualstudiomagazine.com/rss-feeds/columns.aspx");
            //list.add("https://www.xkcd.com/rss.xml");
            //list.add("http://feeds.feedburner.com/d0od?format=xml");
            feedUrls.postValue(list);
        }
        return feedUrls;
    }


    private MutableLiveData<Set<Article>> favorites;
    public LiveData<Set<Article>> getFavorites() {
        if (favorites == null){
            favorites = new MutableLiveData<>();

            favorites.postValue(new HashSet<>());
        }
        return favorites;
    }

    public void toggleFavorite(Article article){
        if(getFavorites().getValue().contains(article)){
            removeFavorite(article);
        } else {
            addFavorite(article);
        }
    }

    public void addFavorite(Article article){
        Set<Article> value = getFavorites().getValue();
        if(value != null && value.add(article)){
            Toast.makeText(null, "Article favorited", Toast.LENGTH_SHORT).show();
            article.setFavorite(true);
            favorites.postValue(value);
        }
    }

    private void removeFavorite(Article article){
        Set<Article> value = getFavorites().getValue();
        if(value != null && value.remove(article)){
            Toast.makeText(null, "Article favorited", Toast.LENGTH_SHORT).show();
            article.setFavorite(false);
            favorites.postValue(value);
        }
    }

    private static class FetchRssTask extends AsyncTask<String, Void, List<Article>> {

        WeakReference<NewsViewModel> ViewModelReference;

        public FetchRssTask(NewsViewModel reference) {
            ViewModelReference = new WeakReference<>(reference);
        }

        @Override
        protected List<Article> doInBackground(String... urls) {

            List<Article> articles = new ArrayList<>();

            for (String url :
                    urls) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();

                try{
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        if (response.body() != null) {
                            articles.addAll(RssParser.parseXML(response.body().string()));
                        }
                    }
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            return articles;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            ViewModelReference.get().articles.postValue(articles);
        }

    }
}
