package com.softment.thymbol.Utils;

import android.app.Application;

import com.cloudinary.android.MediaManager;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Map config = new HashMap();
        config.put("cloud_name", "thymbol");
        config.put("api_key","971735825785493");
        config.put("api_secret","vbEJmZMUGQyTBs0dAvcHf_hYjsA");

        MediaManager.init(this, config);
    }
}
