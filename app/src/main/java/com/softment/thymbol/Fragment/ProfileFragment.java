package com.softment.thymbol.Fragment;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.softment.thymbol.BuildConfig;
import com.softment.thymbol.Model.UserModel;
import com.softment.thymbol.NotificationActivity;
import com.softment.thymbol.R;

import com.softment.thymbol.Utils.ProgressHud;
import com.softment.thymbol.Utils.Services;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

public class ProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        view.findViewById(R.id.shareApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Thymbol");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_one)));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });


        //Notification
        view.findViewById(R.id.notification).setOnClickListener(view12 -> startActivity(new Intent(getContext(), NotificationActivity.class)));

        view.findViewById(R.id.rateApp).setOnClickListener(view13 -> {
            Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(),getString(R.string.unabletofindmarket), Toast.LENGTH_LONG).show();
            }
        });



        view.findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.thymbolmorocco.com/privacy-policy"));
                startActivity(browserIntent);
            }
        });
        
        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Services.logout(getContext());
            }
        });

        view.findViewById(R.id.help_cener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "admin@thymbol.com" });
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        view.findViewById(R.id.termsAndConditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.thymbolmorocco.com/termsandconditions"));
                startActivity(browserIntent);
            }
        });

        //VersionCode
        TextView version = view.findViewById(R.id.version);
        String versionName = BuildConfig.VERSION_NAME;
        version.setText(versionName);


        //SetupProfile
        RoundedImageView profilePic = view.findViewById(R.id.profilePic);
        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);


        if (!UserModel.data.getProfilePic().isEmpty()) {
            Glide.with(getContext()).load(UserModel.data.getProfilePic()).placeholder(R.drawable.profile_placeholder).into(profilePic);
        }

        name.setText(UserModel.data.getFullName());
        email.setText(UserModel.data.getEmail());


        return view;
    }
}
