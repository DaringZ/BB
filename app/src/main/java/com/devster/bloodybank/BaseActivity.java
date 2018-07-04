package com.devster.bloodybank;

import android.support.v7.app.AppCompatActivity;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.Database.SharedPreference;
import com.tapadoo.alerter.Alerter;

import net.steamcrafted.loadtoast.LoadToast;

/**
 * Created by MOD on 5/5/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private final String  TAG="BaseActivity";
    private FirebaseConn conn;
    private SharedPreference storage;
    private final LoadToast mytoast=new LoadToast(this);


    public void showToasty(String text){
        mytoast.show();
    }
    public void showSuccecs(){
        mytoast.success();
    }
    public void showErroe(){
        mytoast.error();
    }
    public void hideToasty(){
        mytoast.hide();
    }
    public void showAlerter(){
        Alerter.create(this)
                .setText("No Internet Connectivity")
                .setBackgroundColorRes(R.color.color_error)
                .setDuration(1500)
                .setIcon(R.mipmap.wifi_alert)
                .show();
    }
    public void setStorage(SharedPreference preference){
        this.storage=preference;
    }

    public SharedPreference getStorage(){
        return storage;
    }

    public void setConn(FirebaseConn conn) {
        this.conn = conn;
    }

    public FirebaseConn getConn() {
        return conn;
    }
}
