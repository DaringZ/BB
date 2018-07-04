package com.devster.bloodybank.Helpers.Interfaces;

/**
 * Created by MOD on 7/1/2018.
 */

public interface CallBackMainTo {
    public boolean isNetworkAvailable();
    public void loadToasty(String text);
    public void stopToasty(int stateCode);
}
