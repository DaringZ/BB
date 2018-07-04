package com.devster.bloodybank.Helpers.EventBuses;

import android.util.Log;

/**
 * Created by MOD on 7/2/2018.
 */

public class AddNotification {

    private final String TAG=AddNotification.class.getSimpleName();
    private String reqBlood,title,contact;
    private boolean recieve=false;
    public AddNotification(String reqBlood, String title, String contact) {
        Log.d(TAG,"Recieved notification");
        this.reqBlood = reqBlood;
        this.title = title;
        this.contact = contact;
        this.recieve=true;
    }

    public String getReqBlood() {
        return reqBlood;
    }


    public String getTitle() {
        return title;
    }


    public String getContact() {
        return contact;
    }

    public boolean isRecieve() {
        return recieve;
    }

}
