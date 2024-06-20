package com.softment.thymbol.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.softment.thymbol.Adapter.DiscoverCategoryDetailsAdapter;
import com.softment.thymbol.DiscoverDetailsActivity;
import com.softment.thymbol.Model.Voucher;
import com.softment.thymbol.R;

import java.util.ArrayList;
import java.util.Date;


public class MyVoucherFragment extends Fragment {
    private DiscoverCategoryDetailsAdapter discoverCategoryDetailsAdapter;
    private AppCompatButton upcomingBtn, expiredBtn;
    private ArrayList<Voucher> vouchers;
    private TextView no_featurd_location_available;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myvoucher, container, false);

        upcomingBtn = view.findViewById(R.id.upcomingBtn);
        expiredBtn = view.findViewById(R.id.expiredBtn);

        upcomingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyVoucher(FirebaseAuth.getInstance().getCurrentUser().getUid(),"upcoming");
                upcomingBtn.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.activebtn,null));
                expiredBtn.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.inactivebtn,null));

            }
        });

        expiredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyVoucher(FirebaseAuth.getInstance().getCurrentUser().getUid(),"expired");

                upcomingBtn.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.inactivebtn,null));
                expiredBtn.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.activebtn,null));
            }
        });

        no_featurd_location_available = view.findViewById(R.id.no_featured_location_available);

        vouchers = new ArrayList<>();
        discoverCategoryDetailsAdapter = new DiscoverCategoryDetailsAdapter(getContext(), vouchers);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(discoverCategoryDetailsAdapter);

        getMyVoucher(FirebaseAuth.getInstance().getCurrentUser().getUid(),"upcoming");
        return view;
    }

    public void getMyVoucher(String uid, String status){

       Query query = FirebaseFirestore.getInstance().collection("GenerateVouchers")
                .orderBy("voucherExpireDate", Query.Direction.DESCENDING)
                .whereEqualTo("userId",uid);

                if (status.equals("upcoming")) {
                    query = query.whereGreaterThanOrEqualTo("voucherExpireDate",new Date());
                }
                else {
                    query = query.whereLessThan("voucherExpireDate",new Date());
                }
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            vouchers.clear();
                            if (value != null && !value.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
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

                    }
                });
    }

}
