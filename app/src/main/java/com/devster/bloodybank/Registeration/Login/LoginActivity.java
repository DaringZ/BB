package com.devster.bloodybank.Registeration.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.devster.bloodybank.BaseActivity;
import com.devster.bloodybank.Helpers.EventBuses.UpdateUI;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Registeration.SignUp.Registration;
import com.devster.bloodybank.Utils.TextValidation;
import com.devster.bloodybank.Views.Main.MainActivity;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG="LoginAvtivity";
    private ScrollView register_page1;
    private CountryCodePicker ccp;
    private EditText et_phnNumber;
    private TextView tv_verify;
    private Button signIn,signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        InitializePreference(this);
        setFireConn(LoginActivity.this);
        init();
        hideKeyboard();




    }

    private void startRegistration() {
        Intent intent=new Intent(LoginActivity.this, Registration.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (isLogged()) {
            startMainActivity();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLogged()) {
            startMainActivity();
        }
    }

    private boolean isLogged() {
        return getConn().getCurrentUser() != null;
    }
    private void init() {

        register_page1 = findViewById(R.id.login_panel);
        ccp = findViewById(R.id.ccp);
        et_phnNumber =     findViewById(R.id.et_phnNo);
        ccp.registerCarrierNumberEditText(et_phnNumber);
        tv_verify =     findViewById(R.id.tv_verify);
        signIn =     findViewById(R.id.btn_signin);
        signUp=findViewById(R.id.btn_signup);
        setListners();
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    private void setListners() {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(LoginActivity.this, "Country Code " + ccp.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();

            }
        });
        et_phnNumber.setOnClickListener(this);

        et_phnNumber.addTextChangedListener(new TextValidation() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                String completeOldText = before + old + after;
                String completeNewText = before + aNew + after;
                Log.i(TAG, "completeOld\n" + "before: " + before + "old: " + old + "\nafter: " + after);
                Log.i(TAG, "completeNew\n" + "before: " + before + "new: " + aNew + "\nafter: " + after);

                if (completeNewText.length() == 1 && completeNewText.equals("0")) {
                    et_phnNumber.setText("");
                    et_phnNumber.setError("cannot start with 0");

                }
                if (completeNewText.length() < 10){
                    if(signIn.isEnabled())
                        signIn.setEnabled(false);

                }


                if (completeNewText.contains(" ")) {
                    startUpdates();
                    et_phnNumber.setText("");
                    et_phnNumber.append(completeNewText.replaceAll("\\s", ""));
                    et_phnNumber.setError("no whitespaces");
                    endUpdates();
                }
                if (completeNewText.length() > 12) {
                    et_phnNumber.setError("Invalid Number");
                }
                if (completeNewText.length() == 10 || completeNewText.length() == 11) {
                    signIn.setEnabled(true);
                    startUpdates();
                    hideKeyboard();
                    endUpdates();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_signin:
                showToasty(LoginActivity.this,"Signing In",1660,getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.white));
                signIn();
                break;
            case R.id.btn_signup:
                Intent intent=new Intent(LoginActivity.this, Registration.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

        }

    }
    private void signIn() {
        String Fullnum=ccp.getSelectedCountryCodeWithPlus()+et_phnNumber.getText().toString();
        if(!TextUtils.isEmpty(Fullnum)){
            getConn().signIn(Fullnum);
        }
        else{
            et_phnNumber.setError("Field is required");
        }
    }

    private void startMainActivity(){
        Intent intent=new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUI stata){
         final int SIGNIP_SUCCESS_CODE = 1000;
        final int SIGNIP_Faluir_CODE=-1000;
        switch(stata.getState()){
            case SIGNIP_SUCCESS_CODE:
                showSuccecs();
                startMainActivity();
                break;
            case SIGNIP_Faluir_CODE:
                showError();
        }
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
