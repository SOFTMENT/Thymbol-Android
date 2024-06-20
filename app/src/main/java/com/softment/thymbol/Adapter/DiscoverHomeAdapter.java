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

public class DiscoverHomeAdapter extends RecyclerView.Adapter<DiscoverHomeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<StoreModel> storeModels;

    public DiscoverHomeAdapter(Context context,ArrayList<StoreModel> storeModels) {
        this.context = context;
        this.storeModels = storeModels;
    }

    @NonNull
    @Override
    public DiscoverHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_store_home_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverHomeAdapter.MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        StoreModel storeModel = storeModels.get(position);

        if (!storeModel.getStoreLogo().isEmpty()) {
            Glide.with(context).load(storeModel.getStoreLogo()).placeholder(R.drawable.placeholder).into(holder.storeImage);
        }
        holder.storeName.setText(storeModel.getStoreName());
        holder.storeAddress.setText(storeModel.getStoreAddress());
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
        private RoundedImageView storeImage;
        private TextView storeName, storeAddress;
        private View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            storeImage = view.findViewById(R.id.image);
            storeName = view.findViewById(R.id.title);
            storeAddress = view.findViewById(R.id.address);

        }
    }
}
