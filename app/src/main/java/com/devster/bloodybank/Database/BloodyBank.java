package com.devster.bloodybank.Database;

import android.app.Application;
import com.firebase.client.Firebase;

/**
 * Created by MOD on 5/5/2018.
 */

public class BloodyBank extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
