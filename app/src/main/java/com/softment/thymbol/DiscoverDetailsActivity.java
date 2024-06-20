package com.softment.thymbol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.softment.thymbol.Adapter.DiscoverCategoryDetailsAdapter;
import com.softment.thymbol.Model.DiscoverCategoryModel;
import com.softment.thymbol.Model.StoreModel;
import com.softment.thymbol.Model.UserModel;
import com.softment.thymbol.Model.Voucher;
import com.softment.thymbol.Utils.ProgressHud;
import com.softment.thymbol.Utils.Services;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class DiscoverDetailsActivity extends AppCompatActivity {

    private DiscoverCategoryDetailsAdapter discoverCategoryDetailsAdapter;
    private TextView no_featurd_location_available;
    private StoreModel storeModel;
    private ArrayList<Voucher> vouchers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_details);

        storeModel = (StoreModel) getIntent().getSerializableExtra("storeModel");
        if (storeModel == null){
            finish();
        }

        TextView title = findViewById(R.id.title);
        title.setText(storeModel.getStoreName().toUpperCase());

        no_featurd_location_available = findViewById(R.id.no_featured_location_available);

        vouchers = new ArrayList<>();
        discoverCategoryDetailsAdapter = new DiscoverCategoryDetailsAdapter(this, vouchers);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(DiscoverDetailsActivity.this,2));
        recyclerView.setAdapter(discoverCategoryDetailsAdapter);

        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });


        //GET
        getAllDiscoverLocation();



    }




    public void getAllDiscoverLocation() {
        ProgressHud.show(this,getString(R.string.loading));
        Query query = null;


        query = FirebaseFirestore.getInstance().collection("Stores").document(storeModel.getStoreId())
                .collection("Vouchers").orderBy("voucherExpireDate").whereGreaterThan("voucherExpireDate",new Date())
                .orderBy("voucherCreateDate", Query.Direction.DESCENDING);


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    vouchers.clear();
                    if (task.getResult() != null && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            Voucher voucher = documentSnapshot.toObject(Voucher.class);
                            vouchers.add(voucher);
                        }
                    }

                    if (vouchers.size() > 0) {
                        no_featurd_location_available.setVisibility(View.GONE);
                    }
                    else {
                        no_featurd_location_available.setVisibility(View.VISIBLE);
                    }
                    discoverCategoryDetailsAdapter.notifyDataSetChanged();
                }
                else {
                    Services.showDialog(DiscoverDetailsActivity.this,getString(R.string.ERROR),task.getException().getLocalizedMessage());
                }
            }
        });

    }
}
