package com.softment.thymbol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.softment.thymbol.Adapter.ReviewAdapter;
import com.softment.thymbol.Model.ReviewModel;
import com.softment.thymbol.Model.UserModel;
import com.softment.thymbol.Model.Voucher;
import com.softment.thymbol.Utils.ProgressHud;
import com.softment.thymbol.Utils.Services;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VoucherHomeActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private Voucher voucher;
    private ReviewAdapter reviewAdapter;
    private final ArrayList<ReviewModel> reviewModels = new ArrayList<>();
    private RelativeLayout ratingAndReviewMainView;
    private TextView totalRatings;
    private RatingBar totalRatingBar;
    private TextView basedOnText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_home);

        voucher = (Voucher) getIntent().getSerializableExtra("voucher");
        if (voucher == null) {
            finish();
        }



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng latLng = new LatLng(voucher.getLatitude(), voucher.getLongitude());
                mMap = googleMap;
                mMap.addMarker(new
                        MarkerOptions().position(latLng).title(voucher.getShopName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        });

        totalRatingBar = findViewById(R.id.ratingBar);
        totalRatings = findViewById(R.id.totalRatings);
        basedOnText = findViewById(R.id.basedOnTotalReview);

        ratingAndReviewMainView = findViewById(R.id.ratingsAndReviewMainView);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView title = findViewById(R.id.title);
        title.setText(voucher.getShopName());

        TextView validity = findViewById(R.id.validity);
        validity.setText(String.format("%s %s", getString(R.string.Validuntil), Services.convertDateToStringWithoutDash(voucher.getVoucherExpireDate())));

        RoundedImageView shopImage = findViewById(R.id.businessImage);
        if (!voucher.getShopLogo().isEmpty()) {
            Glide.with(this).load(voucher.getShopLogo()).placeholder(R.drawable.placeholder).into(shopImage);
        }

        TextView offerTitle = findViewById(R.id.offerTitle);
        TextView voucherOffBottom = findViewById(R.id.voucherOffBottom);

        if (voucher.isFree()) {
            offerTitle.setText(String.format("%s %s", getString(R.string.Get), voucher.getFreeMessage()));
            voucherOffBottom.setText(voucher.getFreeMessage());

        }
        else {
            offerTitle.setText(String.format(Locale.ENGLISH,"%s %d%s", getString(R.string.Get), voucher.getDiscount(), getString(R.string.OFF)));

            voucherOffBottom.setText(String.format(Locale.ENGLISH,"%d%s", voucher.getDiscount(), getString(R.string.OFF)));
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(this,reviewModels);
        recyclerView.setAdapter(reviewAdapter);

        TextView reviewVideoBtn = findViewById(R.id.videoReview);

        ProgressHud.show(this,"");
        FirebaseFirestore.getInstance().collection("Stores").document(voucher.getShopId()).collection("Reviews").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ProgressHud.dialog.dismiss();
                if (error != null) {
                    Services.showDialog(VoucherHomeActivity.this,"ERROR",error.getLocalizedMessage());
                }
                else {
                    reviewModels.clear();
                    double total = 0;
                    if(value != null && !value.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            ReviewModel reviewModel = documentSnapshot.toObject(ReviewModel.class);
                            if (reviewModel.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                reviewVideoBtn.setVisibility(View.GONE);
                            }

                            total = total + reviewModel.rating;
                            reviewModels.add(reviewModel);
                        }

                    }
                    
                    if (reviewModels.size() > 0) {
                        basedOnText.setText("Based on "+reviewModels.size()+" ratings");
                        double avrRatings = total / reviewModels.size();
                        totalRatings.setText(String.format("%.1f",avrRatings));
                        totalRatingBar.setRating((float) avrRatings);
                        ratingAndReviewMainView.setVisibility(View.VISIBLE);
                    }
                    else {
                        ratingAndReviewMainView.setVisibility(View.GONE);
                    }
                    reviewAdapter.notifyDataSetChanged();
                }
            }
        });


        findViewById(R.id.videoReview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoucherHomeActivity.this, ReviewActivity.class);
                intent.putExtra("voucherModel",voucher);
                startActivity(intent);
            }
        });

        TextView businessName = findViewById(R.id.businessName);
        TextView addressName = findViewById(R.id.addressName);
        TextView fullAddress = findViewById(R.id.fullAddress);
        TextView voucherConditions = findViewById(R.id.voucherConditions);

        businessName.setText(voucher.getShopName());
        addressName.setText(voucher.getAddressName());
        fullAddress.setText(voucher.getAddress());
        voucherConditions.setText(voucher.getVoucherConditions());


        //Redeem
        findViewById(R.id.redeemVoucherBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressHud.show(VoucherHomeActivity.this,getString(R.string.generatingvoucher));
                voucher.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                voucher.voucherGenerateDate = new Date();
                String voucherRedeemId = FirebaseFirestore.getInstance().collection("GenerateVouchers").document().getId();
                voucher.voucherRedeemId = voucherRedeemId;

                FirebaseFirestore.getInstance().collection("GenerateVouchers").document(voucherRedeemId).set(voucher).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ProgressHud.dialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(VoucherHomeActivity.this,ShowVoucherActivity.class);
                            intent.putExtra("voucher",voucher);
                            startActivity(intent);
                        }
                        else {
                            Services.showDialog(VoucherHomeActivity.this,getString(R.string.ERROR),task.getException().getLocalizedMessage());
                        }
                    }
                });

            }
        });

    }
}
