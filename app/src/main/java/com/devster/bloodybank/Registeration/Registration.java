package com.devster.bloodybank.Registeration;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Toast;


import com.devster.bloodybank.AppUser.User;
import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.EventBuses.RegisterUserInfo;
import com.devster.bloodybank.EventBuses.UpdateUI;
import com.devster.bloodybank.Fragments.SignUpPhases.Portrait.Phase1;
import com.devster.bloodybank.Fragments.SignUpPhases.Portrait.Phase2;
import com.devster.bloodybank.MainActivity;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Database.SharedPreference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Registration.class.getSimpleName();
    final public int SIGNUP_SUCCESS_CODE=200;
    private final int SIGNUP_ALREADY_CODE = 300;
    private EventBus eventBus = EventBus.getDefault();
    SharedPreference preference;
    FirebaseConn firebaseConn;
    Phase1 phase1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        firebaseConn=new FirebaseConn(this);
        preference=new SharedPreference(getApplicationContext());
        if(preference.isRegister()){
            startMainActivity();
        }
        else if(firebaseConn.getCurrentUser()!=null && !preference.isRegister()){
            startPhase2();
        }
        initView();
        registerSubscriber();
        startFragment();


    }


    private void startPhase2() {
        Phase2 phase2=new Phase2();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.frag_content, phase2).addToBackStack("Phase1").commit();
    }


    private void startMainActivity() {
        finish();
        startActivity(new Intent(Registration.this,MainActivity.class));

    }


    private void initView() {
        phase1=new Phase1();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isLogged()&& preference.isRegister()){
            startMainActivity();
        }

    }

    private void startFragment() {

        FragmentTransaction trans=getFragmentManager().beginTransaction();
        trans.add(R.id.frag_content,phase1).commit();
    }

    private void registerSubscriber(){eventBus.getDefault().register(this); }
    private boolean isLogged() {
        return firebaseConn.getCurrentUser()!=null;
    }

    public void Register(final User user){
        final ProgressDialog progressDialog = new ProgressDialog(this,
                android.R.style.ThemeOverlay_Material_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Waiting..");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        firebaseConn.SignUp(user);
                    }
                }, 3000);


    }

    public void verifyPhone(String number,String countryCode){
        User.setPhoneNumberWCode(number);
        User.setCountryCode((countryCode));
        firebaseConn.verifyPhone(number,countryCode);
    }

    public void verifyCode(String code){
        firebaseConn.verifyCode(code);
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RegisterUserInfo state){
        if(state.isComplete()){
            Toast.makeText(this,"Now registering",Toast.LENGTH_SHORT).show();
            if(state.getUser()!=null){
                Register(state.getUser());
            }

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(UpdateUI state){
        switch (state.getState()){
            case SIGNUP_SUCCESS_CODE:
                setResult(RESULT_FIRST_USER);
                preference.setRegister(true);
                startActivity(new Intent(Registration.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return;
            case SIGNUP_ALREADY_CODE:
                setResult(RESULT_OK,new Intent().putExtra("code",SIGNUP_ALREADY_CODE));
                finish();
                return;

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.getDefault().unregister(this);
    }
}
