package com.softment.thymbol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.softment.thymbol.Model.FriendModel;
import com.softment.thymbol.Model.ReviewModel;
import com.softment.thymbol.Model.UserModel;
import com.softment.thymbol.Model.VoucherCategoryModel;
import com.softment.thymbol.PlayVideo;
import com.softment.thymbol.R;
import com.softment.thymbol.Utils.Services;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<ReviewModel> reviewModels;

    public ReviewAdapter(Context context, ArrayList<ReviewModel> reviewModels){
        this.context = context;
        this.reviewModels = reviewModels ;

    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ratings_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {

        holder.setIsRecyclable(true);
        ReviewModel reviewModel = reviewModels.get(position);
        FirebaseFirestore.getInstance().collection("Users").document(reviewModel.uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().exists()) {
                        FriendModel userModel = task.getResult().toObject(FriendModel.class);
                        holder.fullName.setText(userModel.getFullName());
                        Glide.with(context).load(userModel.profilePic).placeholder(R.drawable.profile_placeholder).into(holder.profile);
                    }
                }
            }
        });

        Glide.with(context).load(reviewModel.thumbnail).placeholder(R.drawable.placeholder).into(holder.thumbnail);
        holder.date.setText(Services.convertDateForReview(reviewModel.getDate()));
        holder.ratingBar.setRating((float) reviewModel.rating);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayVideo.class);
                intent.putExtra("link",reviewModel.video);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView profile, thumbnail;
        private TextView fullName,date;
        private RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            fullName = itemView.findViewById(R.id.fullName);
            date = itemView.findViewById(R.id.date);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
