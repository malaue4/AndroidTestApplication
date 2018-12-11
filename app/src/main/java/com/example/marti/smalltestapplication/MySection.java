package com.example.marti.smalltestapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MySection extends StatelessSection {
    List<Article> list;
    String title;
    public MySection(String title, List<Article> list) {
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
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = list.get(position);
        MyItemViewHolder viewHolder = (MyItemViewHolder) holder;
        viewHolder.titleView.setText(article.Title);
        viewHolder.descriptionView.setText(article.Description);
        viewHolder.favoriteView.setChecked(article.isFavorite);
        viewHolder.iconView.setImageBitmap(article.getSource().getIcon());
        viewHolder.linkView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, article.getLink());
            v.getContext().startActivity(intent);
        });
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
        public MyHeaderViewHolder(View view) {
            super(view);
            viewTitle = view.findViewById(R.id.header_text);
        }
    }

    public static class MyItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView iconView;
        public TextView titleView;
        public TextView descriptionView;
        public Switch favoriteView;
        public TextView linkView;

        public MyItemViewHolder(View layout){
            super(layout);
            iconView = layout.findViewById(R.id.itemIcon);
            titleView = layout.findViewById(R.id.itemTitle);
            descriptionView = layout.findViewById(R.id.itemDescription);
            favoriteView = layout.findViewById(R.id.itemFavorite);
            linkView = layout.findViewById(R.id.itemLink);
        }
    }
}
