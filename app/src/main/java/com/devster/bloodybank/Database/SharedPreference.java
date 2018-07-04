package com.devster.bloodybank.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.devster.bloodybank.Models.UserDetails;

/**
 * Created by MOD on 5/1/2018.
 */

public class SharedPreference {

    private static SharedPreference mInstance;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private final String _globalPreferenceName="appPreference";
    private final String _CHECK="check";
    private final String _REGISTER="register";
    private final String _ID="id";
    private final String _PhnNumber="number";
    private final String _NAME="name";
    private final String _BLOODTYPE="type";
    private final String _Gender="gender";
    private final String _EMAIL="email";
    private final String _LAT="latitude";
    private final String _LON="longitude";
    private final String _CITY="city";
    private final String _COUNTRY="country";

    private SharedPreference(){}

    public static SharedPreference getInstance(){
        if(mInstance==null){
            mInstance=new SharedPreference();
        }
        return mInstance;
    }

    public void Initialize(Context appContext){
        this.context=appContext;
        pref=context.getSharedPreferences(_globalPreferenceName,0);
        editor=pref.edit();
    }

    public void setRan(boolean flag){

        editor.putBoolean(_CHECK,flag);
        editor.commit();
    }
    public void setRegister(boolean isRegister){
        editor.putBoolean(_REGISTER,isRegister);
        editor.commit();
    }
    public boolean isRegister(){
        return pref.getBoolean(_REGISTER,false);
    }

    public boolean isAlreadyOpen(){
        return pref.getBoolean(_CHECK,false);

    }
    public void savePhoneDetails(String number){
        editor.putString(_PhnNumber,number);
        editor.commit();
    }
    public void saveUserDetails(UserDetails user){
        editor.putString(_ID,user.getId());
        editor.putString(_NAME,user.getName());
        editor.putString(_EMAIL,user.getEmail());
        editor.putString(_BLOODTYPE,user.getBloodType());
        editor.putString(_Gender,user.getGender());
        editor.putFloat(_LAT,(float)user.getLat());
        editor.putFloat(_LON,(float)user.getLng());
        editor.putString(_CITY,user.getCity());
        editor.putString(_COUNTRY,user.getCountry());
        editor.commit();
    }
    public String getUserID(){
        return pref.getString(_ID,null);
    }
    public String getUserName(){return pref.getString(_NAME,null);}
    public String getUserBloodType(){return pref.getString(_BLOODTYPE,null);}
    public String getuserEmail(){return pref.getString(_EMAIL,null);}
    public String getNumber(){return pref.getString(_PhnNumber,null);}
    public String getGender(){return pref.getString(_Gender,null);}
    public String getCity(){return pref.getString(_CITY,"");}
    public String getCountry(){return pref.getString(_COUNTRY,"");}
    public Double[] getLocation(){
        return new Double[]{(double)pref.getFloat(_LAT,0),(double)pref.getFloat(_LON,0)};
    }

}
