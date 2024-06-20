package com.softment.thymbol.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softment.thymbol.Model.VoucherCategoryModel;
import com.softment.thymbol.R;


import java.util.ArrayList;

public class CategoryVoucherAdapter extends RecyclerView.Adapter<CategoryVoucherAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<VoucherCategoryModel> voucherCategoryModels;
    private int selectedCategoryIndex;
    public CategoryVoucherAdapter(Context context, ArrayList<VoucherCategoryModel> voucherCategoryModels){
        this.context = context;
        this.voucherCategoryModels = voucherCategoryModels;

    }


    @NonNull
    @Override
    public CategoryVoucherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_category_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVoucherAdapter.MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        VoucherCategoryModel voucherCategoryModel = voucherCategoryModels.get(0);
        holder.title.setText(voucherCategoryModel.getName());
        if (position == selectedCategoryIndex) {
            holder.title.setTextColor(context.getResources().getColor(R.color.main_color));
        }
        else {
            holder.title.setTextColor(context.getResources().getColor(R.color.gray));
        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCategoryIndex = holder.getAdapterPosition();
            //    ((ShowVoucherByPlacesActivity)context).getVouchersBy(voucherCategoryModels.get(selectedCategoryIndex).getName());
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return voucherCategoryModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
