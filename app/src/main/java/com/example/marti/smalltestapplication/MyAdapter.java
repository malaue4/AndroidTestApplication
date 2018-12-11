package com.example.marti.smalltestapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MySection.MyItemViewHolder> implements Filterable {
    private List<Article> myData;

    @Override
    public Filter getFilter() {
        return null;
    }

    public MyAdapter(List<Article> myData) {
        this.myData = myData;
    }

    @NonNull
    @Override
    public MySection.MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_section_item, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_section_header, parent, false);
        }
        MySection.MyItemViewHolder vh = new MySection.MyItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MySection.MyItemViewHolder viewHolder, int position) {
        Article article = myData.get(position);
        viewHolder.titleView.setText(article.Title);
        viewHolder.descriptionView.setText(article.Description);
        viewHolder.favoriteView.setChecked(article.isFavorite);
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }
}
