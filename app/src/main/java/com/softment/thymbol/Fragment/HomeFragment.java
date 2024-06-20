package com.softment.thymbol.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.reflect.TypeToken;
import com.softment.thymbol.Adapter.DiscoverHomeAdapter;
import com.softment.thymbol.DiscoverDetailsActivity;
import com.softment.thymbol.MainActivity;
import com.softment.thymbol.Model.CountryModel;
import com.softment.thymbol.Model.StoreModel;
import com.softment.thymbol.Model.VoucherCategoryModel;
import com.softment.thymbol.NotificationActivity;
import com.softment.thymbol.R;
import com.softment.thymbol.SearchActivity;
import com.softment.thymbol.Utils.Constants;
import com.softment.thymbol.Utils.ProgressHud;
import com.softment.thymbol.Utils.Services;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private CardView topShopView;
    private RoundedImageView topShopImage;
    private TextView topShopName, topShopAddress,topOutOf5, topTotalRatings;
    public ArrayList<StoreModel> storeModels;

    StoreModel topStoreModel = null;
    private DiscoverHomeAdapter discoverHomeAdapter;
    private ArrayList<StoreModel> searchStoreModels = new ArrayList<>();
    MainActivity mainActivity;
    String countryCode = "MA";
    private List<VoucherCategoryModel> voucherCategories = new ArrayList<>();
    Client client = new Client("25LVEKF1EW", "d2d0fcc5e04451e3c0a1f71dd33f33e8");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);






        view.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });

        TextView bestPlaceToVisit = view.findViewById(R.id.bestPlacesToVisit);
        bestPlaceToVisit.setText(String.format("%s %s", getString(R.string.best_places_to_visit), Services.convertDateToMonth(new Date())));

        EditText searchEditText = view.findViewById(R.id.searchEditText);


        view.findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchEditText.getText().toString();
                if (!searchText.isEmpty()) {
                    searchEditText.setText("");
                    search(searchText);
                }
            }
        });

        topShopView = view.findViewById(R.id.bestPlaceCard);
        topShopImage = view.findViewById(R.id.shopImage);
        topShopName = view.findViewById(R.id.shopName);
        topShopAddress = view.findViewById(R.id.shopAddress);
        topOutOf5 = view.findViewById(R.id.topViewOutOf5);
        topTotalRatings =  view.findViewById(R.id.topViewTotalRatings);


        topShopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), DiscoverDetailsActivity.class);
                intent.putExtra("storeModel",topStoreModel);
                startActivity(intent);
            }
        });
        storeModels = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        discoverHomeAdapter = new DiscoverHomeAdapter(getContext(),storeModels);
        recyclerView.setAdapter(discoverHomeAdapter);

        getAllCategory();

        view.findViewById(R.id.category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                CharSequence[] items = new CharSequence[voucherCategories.size()];
                for (int i = 0; i < voucherCategories.size(); i++) {
                    items[i] = voucherCategories.get(i).getName();
                }
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedName = voucherCategories.get(which).getName();
                        System.out.println(selectedName);
                        search(selectedName);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return view;
    }

    private void search(String searchText){
        ProgressHud.show(getContext(),getString(R.string.searching));
        Index index = client.getIndex("Stores");
        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(searchText);
        query.setFilters("countryCode:'"+countryCode+"'");


        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                ProgressHud.dialog.dismiss();

                searchStoreModels.clear();

                try {


                    JSONArray jsonArray = jsonObject.getJSONArray("hits");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject hit = jsonArray.getJSONObject(i);

                        Gson gson = new Gson();

                        StoreModel object = gson.fromJson(hit.toString(), StoreModel.class);
                        searchStoreModels.add(object);
                    }


                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra("stores",searchStoreModels);
                    startActivity(intent);



                } catch (JSONException jsonException) {
                    Services.showDialog(getContext(),getString(R.string.ERROR),jsonException.getMessage());
                }

            }
        });
    }
    private void getAllCategory() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("VoucherCategory")
                .orderBy("name")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        voucherCategories.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            VoucherCategoryModel category = document.toObject(VoucherCategoryModel.class);
                            voucherCategories.add(category);
                        }
                    }
                });
    }
    public void getBestOfMonth(String countryCode){
        this.countryCode = countryCode;
        ProgressHud.show(getContext(),"");
        FirebaseFirestore.getInstance().collection("BestOfTheMonth").document(countryCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                   StoreModel storeModel = task.getResult().toObject(StoreModel.class);
                   if (storeModel != null) {

                       topShopView.setVisibility(View.VISIBLE);
                       topShopName.setText(storeModel.getStoreName());
                       topShopAddress.setText(storeModel.getStoreAddress());
                       topOutOf5.setText(storeModel.getOutOf5());
                       topTotalRatings.setText(String.format("(%s %s)", storeModel.getTotalRatings(), getString(R.string.Reviews)));
                       topStoreModel = storeModel;
                       if (!storeModel.getStoreLogo().isEmpty()) {
                           Glide.with(getContext()).load(storeModel.getStoreLogo()).placeholder(R.drawable.placeholder).into(topShopImage);
                       }
                   }
                }
                else {
                    ProgressHud.dialog.dismiss();
                }

                getAllStoresByLocation(countryCode);
            }
        });
    }
    public void getAllStoresByLocation(String countryCode){


        final GeoLocation center = new GeoLocation(Constants.latitude, Constants.longitude);

        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center,99999);

        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = FirebaseFirestore.getInstance().collection("Stores")
                    .orderBy("geoHash")
                    .whereEqualTo("countryCode",countryCode).whereEqualTo("activeSubscription",true);
            tasks.add(q.get());
        }
        ProgressHud.dialog.dismiss();
        storeModels.clear();

        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                StoreModel storeModel = doc.toObject(StoreModel.class);

                                for (StoreModel s : new ArrayList<StoreModel>(storeModels)) {
                                    if (Objects.equals(s.storeId, storeModel.storeId)) {
                                        storeModels.remove(s);
                                    }
                                }
                                storeModels.add(storeModel);

                            }
                        }

                        if (topStoreModel != null) {
                            storeModels.remove(topStoreModel);
                        }

                        mainActivity.loadDataToAroundMe(storeModels);
                        discoverHomeAdapter.notifyDataSetChanged();
                    }
                });


    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;
        (mainActivity).initializeHomeFragment(this);
    }
}
