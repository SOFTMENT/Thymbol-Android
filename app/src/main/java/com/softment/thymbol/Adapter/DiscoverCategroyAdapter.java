package com.softment.thymbol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softment.thymbol.DiscoverDetailsActivity;
import com.softment.thymbol.Model.DiscoverCategoryModel;
import com.softment.thymbol.R;

import java.util.ArrayList;

public class DiscoverCategroyAdapter extends RecyclerView.Adapter<DiscoverCategroyAdapter.MyViewHolder> {

    private ArrayList<DiscoverCategoryModel> discoverCategoryModels;
    private Context context;

    public DiscoverCategroyAdapter(Context context,ArrayList<DiscoverCategoryModel> discoverCategoryModels ) {
        this.context = context;
        this.discoverCategoryModels = discoverCategoryModels;
    }

    @NonNull
    @Override
    public DiscoverCategroyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_category_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverCategroyAdapter.MyViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            DiscoverCategoryModel discoverCategoryModel = discoverCategoryModels.get(position);
            if (!discoverCategoryModel.getBannerImage().isEmpty()) {
                Glide.with(context).load(discoverCategoryModel.getBannerImage()).placeholder(R.drawable.placeholder1).into(holder.bannerImage);
            }

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,DiscoverDetailsActivity.class);
                    intent.putExtra("discoverCategory",discoverCategoryModel);
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return discoverCategoryModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView bannerImage;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            bannerImage = view.findViewById(R.id.bannner);

        }
    }
}
