package com.devster.bloodybank.EventBuses;

import com.devster.bloodybank.AppUser.User;

/**
 * Created by MOD on 5/8/2018.
 */

public class RegisterUserInfo {

    private  boolean PHASES_COMPLETE=false;
    private String mName,mEmai,mBloodType;
    private int age;
    private double lat,lng;
    private boolean adult;
    private User user;


    public RegisterUserInfo(String name, String email, String bloodType, int age, boolean adult, double latitude, double longitude){
        this.mName=name;
        this.mEmai=email;
        this.mBloodType=bloodType;
        this.age=age;
        this.adult=adult;
        this.lat=latitude;
        this.lng=longitude;
        PHASES_COMPLETE=true;
    }


    public boolean isComplete(){
        if(PHASES_COMPLETE){
            user=new User(mName,mEmai,mBloodType,age,adult,lat,lng);
            return true;
        }
        else
            return false;
    }

    public User getUser(){
        if(user!=null)
            return user;

        return null;
    }
}
