package com.devster.bloodybank.EventBuses;

/**
 * Created by MOD on 5/7/2018.
 */

public class UpdateUI {

    int uiState;
    String msg;

    public UpdateUI(int state,String msg){
        this.uiState=state;
        this.msg=msg;
    }

    public int getState(){return this.uiState;}
    public String getmsg(){
        return this.msg;
    }
}
