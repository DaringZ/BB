package com.devster.bloodybank;

import android.app.Application;
import android.util.Log;

import com.firebase.client.Firebase;

/**
 * Created by MOD on 7/3/2018.
 */

public class BloodyBank extends Application {

    private final String  TAG="BloodyBank";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate-> "+"Set Firebase Context");
        Firebase.setAndroidContext(this);
    }
}
