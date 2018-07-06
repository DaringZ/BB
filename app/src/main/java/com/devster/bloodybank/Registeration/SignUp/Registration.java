package com.devster.bloodybank.Registeration.SignUp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.devster.bloodybank.BaseActivity;
import com.devster.bloodybank.Helpers.EventBuses.UpdateUI;
import com.devster.bloodybank.Helpers.Interfaces.CallbackRegisterTo;
import com.devster.bloodybank.Models.UserDetails;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Views.Main.MainActivity;
import com.devster.bloodybank.Views.Phases.Portrait.Phase1;
import com.devster.bloodybank.Views.Phases.Portrait.Phase2;
import com.devster.bloodybank.Views.splashScreen;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Registration extends BaseActivity implements CallbackRegisterTo {

    private static final String TAG = Registration.class.getSimpleName();

    @Override
    public void sendPhoneDetailsForVerify(String fullNumber, String countryCode) {
        getAppUser().setPhoneNumberWCode(fullNumber);
        getAppUser().setCountryCode(countryCode);
        verifyPhone(fullNumber);

    }

    @Override
    public void verifySentCode(String code) {
        verifyCode(code);
    }

    @Override
    public void sendUserDetailsForRegistering(String name, String bloodType, int age,boolean isAdult, String gender, String email, double lat, double lng,String city,String country) {

        getAppUser().setName(name);
        getAppUser().setBloodType(bloodType);
        getAppUser().setAge(age);
        getAppUser().setGender(gender);
        getAppUser().setEmail(email);
        getAppUser().setLat(lat);
        getAppUser().setLng(lng);
        getAppUser().setCity(city);
        getAppUser().setCountry(country);
        getAppUser().setAdult(isAdult);

        Register(getAppUser());

    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        boolean flag=false;
        if(cm.getActiveNetworkInfo()!=null){
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                flag=true;
            } else {
                showAlerter();
                flag=false;
            }
        }
        else
            flag=false;

        return flag;
    }

    @Override
    public void showNetworkAlert() {
        showAlerter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        InitializePreference(this);
        setFireConn(this);
        if (getConn().getCurrentUser() == null) {
            startPhase1();
        } else if (getConn().getCurrentUser() != null && !getPreference().isRegister()) {
            startPhase2();
        }
        EventBus.getDefault().register(this);


    }


    private void startSplashScreen() {
        finish();
        startActivity(new Intent(Registration.this, splashScreen.class));
    }

    private void startMainActivity() {
        finish();
        startActivity(new Intent(Registration.this, MainActivity.class));

    }

    private void startPhase1() {

        Phase1 phase1 = new Phase1();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.add(R.id.frag_content, phase1).commit();
    }

    private void startPhase2() {
        Phase2 phase2 = new Phase2();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.frag_content, phase2).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( getConn().getCurrentUser() != null && getPreference().isRegister()) {
            startMainActivity();
        }

    }




    public void verifyPhone(String number) {
        getConn().verifyPhone(number);
    }

    public void verifyCode(String code) {
        getConn().verifyCode(code);
    }

    public void Register(final UserDetails user) {
        showToasty(this,"This may take a while.",1400,Color.GREEN,Color.WHITE);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        getConn().SignUp(user);
                    }
                }, 1500);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(UpdateUI state) {
        final int SIGNUP_SUCCESS_CODE = 200;
        final int SIGNUP_ALREADY_CODE = 300;
        switch (state.getState()) {
            case SIGNUP_SUCCESS_CODE:
                setResult(RESULT_FIRST_USER);
                getPreference().setRegister(true);
                showSuccecs();
                finish();
                startActivity(new Intent(Registration.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return;
            case SIGNUP_ALREADY_CODE:
                showError();
                setResult(RESULT_OK, new Intent().putExtra("code", SIGNUP_ALREADY_CODE));
                finish();
                return;
        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }


}
