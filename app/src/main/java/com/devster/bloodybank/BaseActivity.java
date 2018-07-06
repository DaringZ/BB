package com.devster.bloodybank;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.Database.SharedPreference;
import com.devster.bloodybank.Models.UserDetails;
import com.tapadoo.alerter.Alerter;

import net.steamcrafted.loadtoast.LoadToast;

/**
 * Created by MOD on 5/5/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private final String TAG = "BaseActivity";
    private Context context;
    private final FirebaseConn conn = FirebaseConn.getInstance();
    final SharedPreference preference = SharedPreference.getInstance();
    private  LoadToast mytoast;
    private final UserDetails appUser = new UserDetails();


    public  BaseActivity(){
    }
    public UserDetails getAppUser() {
        return appUser;
    }




    public void showToasty(Context ctxt,String text, int offset, int color, int borderColor) {
        mytoast = new LoadToast(ctxt);
        mytoast.setText(text)
                .setProgressColor(getResources().getColor(R.color.white))
        .setTranslationY(offset).setBackgroundColor(color).setBorderColor(borderColor);
        mytoast.show();
    }

    public void showSuccecs() {
        mytoast.success();
    }

    public void showError() {
        mytoast.error();
    }

    public void hideToasty() {
        mytoast.hide();
    }

    public void showAlerter() {
        Alerter.create(this)
                .setText("No Internet Connectivity")
                .setBackgroundColorRes(R.color.color_error)
                .setDuration(1500)
                .setIcon(R.mipmap.wifi_alert)
                .show();
    }

    public void InitializePreference(Context ctxt) {
        preference.Initialize(ctxt);
    }

    public final SharedPreference getSliderManager() {
        return preference;
    }

    public final SharedPreference getPreference() {
        return preference;
    }

    public void setFireConn(Activity act) {
        conn.Initialize(act);
    }

    public FirebaseConn getConn() {
        return conn;
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}
