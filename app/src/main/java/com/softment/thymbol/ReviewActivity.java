package com.softment.thymbol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.softment.thymbol.Interface.VideoDownloadLinkCallback;
import com.softment.thymbol.Model.ReviewModel;
import com.softment.thymbol.Model.UserModel;
import com.softment.thymbol.Model.Voucher;
import com.softment.thymbol.Utils.ProgressHud;
import com.softment.thymbol.Utils.Services;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {
    private ProgressDialog progressBar;
    private LinearLayout uploadVideoBtn;
    private Uri uri = null;
    private TextView uploadVideoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        uploadVideoText = findViewById(R.id.uploadVideoText);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);

        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        RoundedImageView roundedImageView = findViewById(R.id.profile);
        TextView fullName = findViewById(R.id.fullName);

        Glide.with(this).load(UserModel.data.profilePic).placeholder(R.drawable.profile_placeholder).into(roundedImageView);
        fullName.setText(UserModel.data.getFullName());

        RatingBar ratingBar = findViewById(R.id.ratingBar);

        uploadVideoBtn = findViewById(R.id.uploadVideoBtn);

        uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, 100);
            }
        });

        Voucher voucher = (Voucher) getIntent().getSerializableExtra("voucherModel");
        TextView storeName = findViewById(R.id.storeName);
        storeName.setText(voucher.shopName);
        findViewById(R.id.postBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() > 0) {
                    if (uri == null) {
                        Services.showCenterToast(ReviewActivity.this,"Upload Video");
                    }
                    else {
                        ReviewModel reviewModel = new ReviewModel();
                        reviewModel.id = FirebaseFirestore.getInstance().collection("Stores").document(voucher.getShopId()).collection("Reviews").document().getId();
                        uploadVideOnCloudinary(uri, reviewModel.id, new VideoDownloadLinkCallback() {
                            @Override
                            public void onCallBack(String downloadURL, double duration) {
                                if (downloadURL.isEmpty()) {
                                    Services.showCenterToast(ReviewActivity.this,"Upload Failed");
                                }
                                else {
                                    ProgressHud.show(ReviewActivity.this,"");
                                    reviewModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    reviewModel.rating = (double) ratingBar.getRating();
                                    reviewModel.date = new Date();
                                    reviewModel.video = downloadURL;
                                    reviewModel.storeId = voucher.shopId;
                                    reviewModel.thumbnail = downloadURL.replace(".mov",".jpg");
                                    FirebaseFirestore.getInstance().collection("Stores").document(voucher.getShopId()).collection("Reviews").document(reviewModel.getId()).set(reviewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Services.showCenterToast(ReviewActivity.this,"Thank You for your rating.");
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                    }
                                                },1500);
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
                else {
                    Services.showCenterToast(ReviewActivity.this,"Select Rating");
                }
            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            uploadVideoBtn.setBackgroundResource(R.drawable.videoreview_success);
            uploadVideoText.setText("Video Uploaded");
            uploadVideoText.setTextColor(getColor(R.color.white));
        }
    }



    private void uploadVideOnCloudinary(Uri videouri, String id, VideoDownloadLinkCallback downloadLinkCallback) {


        if (videouri != null) {

            MediaManager.get().upload(videouri)
                    .option("public_id",id)
                    .option("folder","ReviewVideos")
                    .option("resource_type","video")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            progressBar.setMessage("Uploading Video...");
                            progressBar.show();
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            int percentage = (int) ((bytes*1.0 / totalBytes)*100);
                            progressBar.setProgress(percentage);

                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            progressBar.dismiss();
                            downloadLinkCallback.onCallBack(resultData.get("secure_url").toString(),(double)resultData.get("duration"));

                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Services.showDialog(ReviewActivity.this,"ERROR",error.getDescription());


                            progressBar.dismiss();

                            downloadLinkCallback.onCallBack("",-1);
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Services.showDialog(ReviewActivity.this,"ERROR",error.getDescription());
                            progressBar.dismiss();
                            downloadLinkCallback.onCallBack("",-1);
                        }
                    }).dispatch();




        }
    }
}
