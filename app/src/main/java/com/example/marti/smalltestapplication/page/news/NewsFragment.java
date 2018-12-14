package com.example.marti.smalltestapplication.page.news;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.marti.smalltestapplication.BR;
import com.example.marti.smalltestapplication.R;
import com.example.marti.smalltestapplication.model.Article;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static com.example.marti.smalltestapplication.Utility.RelativeTime;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment {

    private FilterArticlesBy filterBy;
    private NewsViewModel mViewModel;

    public NewsFragment() {
    }

    public enum FilterArticlesBy {
        None,
        Favorites
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewsFragment newInstance(FilterArticlesBy filter) {
        NewsFragment fragment = new NewsFragment();
        fragment.filterBy = filter;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.section_listview);
        recyclerView.setHasFixedSize(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        if(filterBy == FilterArticlesBy.Favorites){
            FilterableSectionAdapter fsa = new FilterableSectionAdapter();
            mViewModel.getArticles().observe(this, articles -> {
                fsa.setOriginalData(articles);
                //setData(recyclerView, articles);
            });
            recyclerView.setAdapter(fsa);
        } else {
            mViewModel.getArticles().observe(this, articles -> {
                setData(recyclerView, articles);
            });
        }
        return rootView;
    }

    private void setData(RecyclerView recyclerView, List<Article> myData) {
        Map<String, List<Article>> datData;
            datData = myData.stream()
                    .sorted(Comparator.comparing(Article::getPublishDate).reversed())
                    .collect(Collectors.groupingBy(
                            article -> RelativeTime(getContext(), article.getPublishDate()),
                            LinkedHashMap::new,
                            Collectors.toList()));


        SectionedRecyclerViewAdapter sectionAdapter = (SectionedRecyclerViewAdapter) recyclerView.getAdapter();
        if(sectionAdapter == null){
            sectionAdapter = new SectionedRecyclerViewAdapter();
            recyclerView.setAdapter(sectionAdapter);
        }

        sectionAdapter.removeAllSections();
        // Add your Sections
        for (String key :
                datData.keySet()) {
            sectionAdapter.addSection(new MySection(key, datData.get(key)));
        }
        recyclerView.setAdapter(sectionAdapter);
    }

    public static class MySection extends StatelessSection {
        List<Article> list;
        String title;
        MySection(String title, List<Article> list) {
            super(SectionParameters.builder()
            .itemResourceId(R.layout.my_section_item)
            .headerResourceId(R.layout.my_section_header)
            .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            if(list == null) return 0;
            else return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {

            ViewGroup parent = (ViewGroup) view;
            LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
            ViewDataBinding vdb = DataBindingUtil.inflate(layoutInflater, R.layout.my_section_item, parent, false);
            return new MyItemViewHolder(vdb);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            Article article = list.get(position);
            MyItemViewHolder viewHolder = (MyItemViewHolder) holder;
            viewHolder.bind(article);
        }

        @Override
        public  RecyclerView.ViewHolder getHeaderViewHolder(View view){
            return new MyHeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            MyHeaderViewHolder headerHolder = (MyHeaderViewHolder) holder;

            headerHolder.viewTitle.setText(title);
        }

        private class MyHeaderViewHolder extends RecyclerView.ViewHolder {

            private final TextView viewTitle;
            MyHeaderViewHolder(View view) {
                super(view);
                viewTitle = view.findViewById(R.id.header_text);
            }
        }

        static class MyItemViewHolder extends RecyclerView.ViewHolder{
            private final ViewDataBinding binding;
            private final ObservableBoolean expanded;

            MyItemViewHolder(ViewDataBinding binding){
                super(binding.getRoot());
                this.binding = binding;
                this.expanded = new ObservableBoolean(false);
                this.binding.getRoot().setOnClickListener((view) -> {
                    expanded.set(!expanded.get());
                });
            }

            public void bind(Article article){
                binding.setVariable(BR.article,article);
                binding.setVariable(BR.isExpanded,expanded);
                binding.executePendingBindings();
            }
        }
    }

    public class FilterableSectionAdapter extends SectionedRecyclerViewAdapter implements Filterable{
        public List<Article> getOriginalData() {
            return originalData;
        }

        public void setOriginalData(List<Article> originalData) {
            this.originalData = originalData;
            for (Article a : originalData){
                a.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        getFilter().filter("");

                    }
                });
            }
        }

        private List<Article>originalData = null;
        private Filter filter = new FavoriteFilter();

        @Override
        public Filter getFilter() {
            return filter;
        }

        private void setData(List<Article> data){
            Map<String, List<Article>> datData;
            datData = data.stream()
                    .sorted(Comparator.comparing(Article::getPublishDate).reversed())
                    .collect(Collectors.groupingBy(
                            article -> RelativeTime(getContext(), article.getPublishDate()),
                            LinkedHashMap::new,
                            Collectors.toList()));

            removeAllSections();
            // Add your Sections
            for (String key :
                    datData.keySet()) {
                addSection(new MySection(key, datData.get(key)));
            }
        }

        private class FavoriteFilter extends Filter{
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                final List<Article> list = originalData.stream().filter(Article::isFavorite).collect(Collectors.toList());
                results.values = list;
                results.count = list.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                setData((List<Article>) results.values);
                //notifyDataSetChanged();
            }
        }
    }
}
