package com.devster.bloodybank.Notifiers.Braodcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devster.bloodybank.Helpers.EventBuses.NetworkStateChanged;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MOD on 7/1/2018.
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    // post event if there is no Internet connection
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            // there is Internet connection
        } else if (ni != null && ni.getState() == NetworkInfo.State.DISCONNECTED)
            // no Internet connection, send network state changed
            EventBus.getDefault().post(new NetworkStateChanged(false));
    }

}