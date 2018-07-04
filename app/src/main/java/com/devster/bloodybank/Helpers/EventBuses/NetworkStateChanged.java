package com.devster.bloodybank.Helpers.EventBuses;

/**
 * Created by MOD on 7/1/2018.
 */

public class NetworkStateChanged {

    private boolean mIsInternetConnected;

    public NetworkStateChanged(boolean isInternetConnected) {
        this.mIsInternetConnected = isInternetConnected;
    }

    public boolean isInternetConnected() {
        return this.mIsInternetConnected;
    }
}
