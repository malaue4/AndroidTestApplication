package com.example.marti.smalltestapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.example.marti.smalltestapplication.Utility.RelativeTime;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private List<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class NewsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public NewsFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static NewsFragment newInstance(int sectionNumber) {
            NewsFragment fragment = new NewsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            RecyclerView recyclerView = rootView.findViewById(R.id.section_listview);
            recyclerView.setHasFixedSize(false);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            new MyDataHelper(recyclerView).execute("https://visualstudiomagazine.com/rss-feeds/columns.aspx");
            return rootView;
        }

        private class MyDataHelper extends FetchRssTask{

            private RecyclerView recyclerView;

            MyDataHelper(RecyclerView recyclerView) {
                this.recyclerView = recyclerView;
            }

            @Override
            protected void onPostExecute(List<Article> articles) {
                setData(recyclerView, articles);
            }

            private void setData(RecyclerView recyclerView, List<Article> myData) {
                Map<String, List<Article>> datData = myData.stream()
                        .sorted(Comparator.comparing(Article::getPublishDate).reversed())
                        .collect(Collectors.groupingBy(
                                article -> RelativeTime(article.PublishDate),
                                LinkedHashMap::new,
                                Collectors.toList()));

                SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();

                // Add your Sections
                for (String key :
                        datData.keySet()) {
                    sectionAdapter.addSection(new MySection(key, datData.get(key)));
                }
                recyclerView.setAdapter(sectionAdapter);
            }
        }
    }

    enum Pages{
        newsPage,
        favoritesPage,
        mapPage
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Pages page = Pages.values()[position];

            switch (page){
                case newsPage:
                    break;
                case favoritesPage:
                    break;
                case mapPage:
                    return MapViewFragment.newInstance();
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a NewsFragment (defined as a static inner class below).
            return NewsFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return Pages.values().length;
        }
    }
}
