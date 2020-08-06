package com.janus.newsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.NewsVH>{

    Context context;
    ArrayList<NewsFormat> data;

    SwipeRefreshLayout swipeRefreshFeed;

    public AdapterNews(Context context, ArrayList<NewsFormat> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public NewsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_news,parent,false);
        NewsVH newsVH = new NewsVH(view);
        return newsVH;
    }


    @Override
    public void onBindViewHolder(@NonNull NewsVH holder, int position) {

        final NewsFormat dataItem = data.get(position);
        holder.txtTitle.setText(dataItem.getNewsTitle());
        holder.txtDesc.setText(dataItem.getNewsDesc());

        Glide.with(context).load(dataItem.getNewsImage()).into(holder.imgNews);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("url",dataItem.getNewsURL());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK); // This was added if the application should be working for android 6
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NewsVH extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDesc;
        ImageView imgNews;

        public NewsVH(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.newsTitle);
            txtDesc = itemView.findViewById(R.id.newsDesc);
            imgNews = itemView.findViewById(R.id.newsImage);

        }
    }

}
