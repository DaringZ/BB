package com.devster.bloodybank.Helpers.EventBuses;

import android.util.Log;

/**
 * Created by MOD on 5/7/2018.
 */

public class UpdateUI {

    private final String TAG=UpdateUI.class.getSimpleName();
    int uiState;
    String msg;

    public UpdateUI(int state,String msg){
        Log.i(TAG,msg);
        this.uiState=state;
        this.msg=msg;
    }

    public int getState(){return this.uiState;}
    public String getmsg(){
        return this.msg;
    }
}
