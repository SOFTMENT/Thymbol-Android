package com.softment.thymbol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softment.thymbol.DiscoverDetailsActivity;
import com.softment.thymbol.Model.StoreModel;
import com.softment.thymbol.R;
import com.makeramen.roundedimageview.RoundedImageView;



import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<StoreModel> storeModels;

    public SearchResultAdapter(Context context,ArrayList<StoreModel> storeModels ){
        this.context = context;
        this.storeModels = storeModels;
    }

    @NonNull
    @Override
    public SearchResultAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        StoreModel storeModel = storeModels.get(position);
        if (!storeModel.getStoreLogo().isEmpty()) {
            Glide.with(context).load(storeModel.getStoreLogo()).placeholder(R.drawable.placeholder).into(holder.image);
        }
        holder.title.setText(storeModel.getStoreName());
        holder.address.setText(storeModel.getStoreAddress());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DiscoverDetailsActivity.class);
                intent.putExtra("storeModel",storeModel);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return storeModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView image;
        private TextView title,address;
        private View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            address = view.findViewById(R.id.address);

        }
    }
}
