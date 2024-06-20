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
import com.softment.thymbol.Model.UserModel;
import com.softment.thymbol.Model.Voucher;
import com.softment.thymbol.R;
import com.softment.thymbol.Utils.Services;
import com.softment.thymbol.VoucherHomeActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class DiscoverCategoryDetailsAdapter extends RecyclerView.Adapter<DiscoverCategoryDetailsAdapter.MyViewHolder> {

    private Context context;
    public ArrayList<Voucher> vouchers;

    public DiscoverCategoryDetailsAdapter(Context context, ArrayList<Voucher> vouchers) {
        this.context = context;
        this.vouchers = vouchers;
    }

    @NonNull
    @Override
    public DiscoverCategoryDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_category_details_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverCategoryDetailsAdapter.MyViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            Voucher voucher = vouchers.get(position);
            holder.title.setText(voucher.getShopName());
            holder.category.setText(voucher.getVoucherCategory());
            if (voucher.isFree()) {
                holder.percentage.setText(voucher.getFreeMessage());
                holder.off.setVisibility(View.GONE);
            }
            else {
                holder.percentage.setText(String.valueOf(voucher.getDiscount()));
                holder.off.setVisibility(View.VISIBLE);
            }
            if (!voucher.getShopLogo().isEmpty()) {
                Glide.with(context).load(voucher.getShopLogo()).placeholder(R.drawable.placeholder).into(holder.imageView);

            }
            holder.view.setOnClickListener(view -> {

                    Intent intent = new Intent(context,VoucherHomeActivity.class);
                    intent.putExtra("voucher",voucher);
                    context.startActivity(intent);



            });
    }


    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View view;
        RoundedImageView imageView;
        TextView title, category, percentage, off;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            category = view.findViewById(R.id.category);
            percentage = view.findViewById(R.id.percentage);
            off = view.findViewById(R.id.off);


        }
    }
}
