package com.devster.bloodybank.Notifiers.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by MOD on 7/2/2018.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private final String TAG=FirebaseIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String mToken= FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG,"Refreshed Token "+mToken);

    }
}
