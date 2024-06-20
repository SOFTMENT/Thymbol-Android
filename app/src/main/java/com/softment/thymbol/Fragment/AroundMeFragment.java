package com.softment.thymbol.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.CameraPosition;
import com.softment.thymbol.MainActivity;
import com.softment.thymbol.Model.StoreModel;
import com.softment.thymbol.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class AroundMeFragment extends Fragment {

    private ArrayList<StoreModel> storeModels;
    private ArrayList<String> storeNames;
    private Context context;
    GoogleMap mMap;
    LinearLayout storeView;
    RoundedImageView storeLogo;
    TextView storeName, storeAddress;
    private StoreModel storeModel;
    private AutoCompleteTextView keywords;
    private ArrayAdapter<String> keywordsAdapter;
    private MainActivity mainActivity;
    public AroundMeFragment() {

    }

    public AroundMeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_around_me, container, false);

        storeView = view.findViewById(R.id.shopView);
        storeLogo = view.findViewById(R.id.shopLogo);
        storeName = view.findViewById(R.id.locationName);
        storeAddress = view.findViewById(R.id.address);

        keywords = view.findViewById(R.id.key);
        keywords.setThreshold(1);
        keywords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (StoreModel storeModel1 : storeModels) {

                    if (storeModel1.getStoreName().equalsIgnoreCase(adapterView.getItemAtPosition(i).toString())) {
                        storeModel = storeModel1;
                        LatLng latLng = new LatLng(storeModel1.getLatitude(), storeModel1.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16f));

                        if (!storeModel1.getStoreLogo().isEmpty()) {
                            Glide.with(getContext()).load(storeModel.getStoreLogo()).placeholder(R.drawable.placeholder).into(storeLogo);

                        }
                        storeName.setText(storeModel1.getStoreName());
                        storeAddress.setText(storeModel1.getStoreAddress());
                        storeView.setVisibility(View.VISIBLE);


                        break;
                    }

                }
            }
        });
        storeNames = new ArrayList<>();;
        keywordsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,storeNames);
        keywords.setAdapter(keywordsAdapter);

        storeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeModel != null) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+storeModel.getLatitude()+","+storeModel.getLongitude());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });

        storeModels = new ArrayList<>();

        MapFragment mapFragment = (MapFragment)(mainActivity).getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                mMap = googleMap;

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {

                        int index = (int) marker.getTag();
                        marker.showInfoWindow();
                        storeModel = storeModels.get(index);
                        if (!storeModel.getStoreLogo().isEmpty()) {
                            Glide.with(getContext()).load(storeModel.getStoreLogo()).placeholder(R.drawable.placeholder).into(storeLogo);

                        }
                        storeName.setText(storeModel.getStoreName());
                        storeAddress.setText(storeModel.getStoreAddress());
                        storeView.setVisibility(View.VISIBLE);


                        return true;
                    }
                });


            }
        });





        return view;
    }

    public void loadAllStores(ArrayList<StoreModel> sModels){

        storeModels.clear();
        storeModels.addAll(sModels);


        storeNames.clear();
        for (StoreModel storeModel1 : storeModels) {
            storeNames.add(storeModel1.storeName);

        }

        keywordsAdapter.notifyDataSetChanged();

            if (storeModels.size() > 0)
                    loadAnotation();

    }

    public void loadAnotation(){
        int i = 0;
        for (StoreModel storeModel : storeModels) {
            LatLng latLng = new LatLng(storeModel.getLatitude(), storeModel.getLongitude());
            Marker marker = mMap.addMarker(new
                    MarkerOptions().position(latLng).title(storeModel.getStoreName()));
            marker.setTag(i);

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    storeView.setVisibility(View.GONE);
                }
            });
           i++;
        }
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(storeModels.get(0).getLatitude(), storeModels.get(0).getLongitude()))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();
       mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        mainActivity.initializeAroundFragment(this);
    }
}


