package com.devster.bloodybank.Registeration.SignUp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.Database.SharedPreference;
import com.devster.bloodybank.Helpers.EventBuses.UpdateUI;
import com.devster.bloodybank.Helpers.Interfaces.CallbackRegisterTo;
import com.devster.bloodybank.Models.UserDetails;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Views.Main.MainActivity;
import com.devster.bloodybank.Views.Phases.Portrait.Phase1;
import com.devster.bloodybank.Views.Phases.Portrait.Phase2;
import com.devster.bloodybank.Views.splashScreen;
import com.tapadoo.alerter.Alerter;

import net.steamcrafted.loadtoast.LoadToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Registration extends AppCompatActivity implements CallbackRegisterTo {


    private final UserDetails appUser = new UserDetails();
    private LoadToast mytoast;


    @Override
    public void sendPhoneDetailsForVerify(String fullNumber, String countryCode) {
        appUser.setPhoneNumberWCode(fullNumber);
        appUser.setCountryCode(countryCode);
        verifyPhone(fullNumber);

    }

    @Override
    public void verifySentCode(String code) {
        verifyCode(code);
    }

    @Override
    public void sendUserDetailsForRegistering(String name, String bloodType, int age,boolean isAdult, String gender, String email, double lat, double lng,String city,String country) {

        appUser.setName(name);
        appUser.setBloodType(bloodType);
        appUser.setAge(age);
        appUser.setGender(gender);
        appUser.setEmail(email);
        appUser.setLat(lat);
        appUser.setLng(lng);
        appUser.setCity(city);
        appUser.setCountry(country);
        appUser.setAdult(isAdult);

        Register(appUser);

    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            showAlerter();
            return false;
        }
    }

    private static final String TAG = Registration.class.getSimpleName();

    private final SharedPreference preference = SharedPreference.getInstance();
    private final FirebaseConn firebaseConn = FirebaseConn.getInstance();
    private final EventBus eventBus = EventBus.getDefault();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        firebaseConn.Initialize(this);
        if (firebaseConn.getCurrentUser() == null) {
            startPhase1();
        } else if (firebaseConn.getCurrentUser() != null && !preference.isRegister()) {
            startPhase2();
        }
        registerSubscriber();
        mytoast=new LoadToast(this)
                .setProgressColor(Color.GREEN)
                .setTranslationY(1000)
                .setBorderColor(Color.WHITE);


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
        if (isLogged() && preference.isRegister()) {
            startMainActivity();
        }

    }

    private boolean isLogged() {
        return firebaseConn.getCurrentUser() != null;
    }

    public void verifyPhone(String number) {
        firebaseConn.verifyPhone(number);
    }

    public void verifyCode(String code) {
        firebaseConn.verifyCode(code);
    }

    public void Register(final UserDetails user) {
        mytoast.setText("This may take a while.");
        mytoast.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        firebaseConn.SignUp(user);
                    }
                }, 2000);


    }

    public void showAlerter(){
        Alerter.create(this)
                .setText("No Internet Connectivity")
                .setBackgroundColorRes(R.color.color_error)
                .setDuration(1500)
                .setIcon(R.mipmap.wifi_alert)
                .show();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(UpdateUI state) {
        final int SIGNUP_SUCCESS_CODE = 200;
        final int SIGNUP_ALREADY_CODE = 300;
        switch (state.getState()) {
            case SIGNUP_SUCCESS_CODE:
                setResult(RESULT_FIRST_USER);
                preference.setRegister(true);
                mytoast.success();
                finish();
                startActivity(new Intent(Registration.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return;
            case SIGNUP_ALREADY_CODE:
                mytoast.hide();
                setResult(RESULT_OK, new Intent().putExtra("code", SIGNUP_ALREADY_CODE));
                finish();
                return;
        }

    }

    @Override
    protected void onDestroy() {
        unsubscride();
        super.onDestroy();

    }

    private void registerSubscriber() {
        eventBus.getDefault().register(this);

    }

    public void unsubscride() {
        eventBus.getDefault().unregister(this);

    }
}
