package com.softment.thymbol;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softment.thymbol.Fragment.AroundMeFragment;
import com.softment.thymbol.Fragment.MyVoucherFragment;
import com.softment.thymbol.Fragment.HomeFragment;
import com.softment.thymbol.Fragment.ProfileFragment;
import com.softment.thymbol.Model.CountryModel;
import com.softment.thymbol.Model.StoreModel;
import com.softment.thymbol.Utils.Constants;
import com.softment.thymbol.Utils.NonSwipeAbleViewPager;
import com.google.android.material.tabs.TabLayout;
import com.softment.thymbol.Utils.ProgressHud;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    private HomeFragment homeFragment;
    private AroundMeFragment aroundMeFragment;
    private TabLayout tabLayout;
    private NonSwipeAbleViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SharedPreferences sharedPreferences;
    List<CountryModel> countryModels = new ArrayList<>();
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getUserLatitudeAndLongitude();
                }
                else {

                }
            });

    private final int[] tabIcons = {
            R.drawable.earth,
            R.drawable.home,
            R.drawable.badge,
            R.drawable.profile,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(Constants.MY_DB, MODE_PRIVATE);
        //ViewPager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CountryModel>>(){}.getType();
        countryModels = gson.fromJson(Constants.countryJSONString, type);
        viewPager.setCurrentItem(1);



    }
    public void getUserLatitudeAndLongitude(){

        ProgressHud.show(this,"");
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (!enabled) {
            allowGPS();
        } else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }

                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
            }).addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Constants.latitude = task.getResult().getLatitude();
                        Constants.longitude = task.getResult().getLongitude();



                        try {
                            String country_name = "";
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            List<Address> addresses = geocoder.getFromLocation(Constants.latitude, Constants.longitude, 1);
                            if(addresses != null && addresses.size() > 0) {
                                country_name = addresses.get(0).getCountryName();
                                String countryCode = "";
                                for (CountryModel countryModel : countryModels) {
                                    if (countryModel.getName().equalsIgnoreCase(country_name)) {
                                        countryCode = countryModel.getCode();

                                        break;
                                    }
                                }
                                if (countryCode.isEmpty()) {


                                }
                                else {

                                    homeFragment.getBestOfMonth(countryCode);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else {
                        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        String countryCodeValue = tm.getNetworkCountryIso();
                      ;
                        homeFragment.getBestOfMonth(countryCodeValue.toUpperCase());
                    }

                }
            });
        }
    }

    public void allowGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        builder.setTitle("GPS");
        builder.setMessage("Please enable gps service.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    public void checkLocationPermission(){

        if (ContextCompat.checkSelfPermission(
                MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            getUserLatitudeAndLongitude();

        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

            explainUser();
        }
        else{
            if (sharedPreferences.getBoolean("isFirstTime",true)) {
                sharedPreferences.edit().putBoolean("isFirstTime",false).apply();
                requestPermissionLauncher.launch(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            else {
                allowFromSettings();
            }

        }
    }
    public void allowFromSettings(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        builder.setTitle("Location Permission");
        builder.setMessage("Please allow location permission from app settings.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 10);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void explainUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        builder.setTitle("Location Permission");
        builder.setMessage("We need your location permission to find near by organizations.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new AroundMeFragment(this));
        viewPagerAdapter.addFrag(new HomeFragment());
        viewPagerAdapter.addFrag(new MyVoucherFragment());
        viewPagerAdapter.addFrag(new ProfileFragment());
        viewPager.setAdapter(viewPagerAdapter);

    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {


            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull @NotNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }



    }

    public void loadDataToAroundMe(ArrayList<StoreModel> storeModels){

        this.aroundMeFragment.loadAllStores(storeModels);
    }

    public void initializeHomeFragment(HomeFragment homeFragment){
        this.homeFragment = homeFragment;

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        homeFragment.getBestOfMonth(countryCodeValue.toUpperCase());
    }

    public void initializeAroundFragment(AroundMeFragment aroundMeFragment){
        this.aroundMeFragment = aroundMeFragment;
    }

}


