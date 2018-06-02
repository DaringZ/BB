package com.devster.bloodybank.Database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MOD on 5/1/2018.
 */

public class SharedPreference {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private final String _FIRST="first";
    private final String _CHECK="check";
    private final String _REGISTER="register";
    private final String _ID="id";



    public SharedPreference(Context context){
        this.context=context;
        pref=context.getSharedPreferences(_FIRST,0);
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
    public  void setUserID(String id){
        editor.putString(_ID,id);
        editor.commit();
    }
    public String getUserID(){
        return pref.getString(_ID,null);
    }

}
