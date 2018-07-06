package com.devster.bloodybank.Views.Phases.Portrait;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.devster.bloodybank.Helpers.EventBuses.UpdateUI;
import com.devster.bloodybank.Helpers.Interfaces.CallbackRegisterTo;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Utils.TextValidation;
import com.hbb20.CountryCodePicker;
import com.tapadoo.alerter.Alerter;

import net.steamcrafted.loadtoast.LoadToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class Phase1 extends Fragment implements View.OnClickListener {

    private static final String TAG = Phase1.class.getSimpleName();
    protected CallbackRegisterTo callbackRegisterTo;

    private Context context;
    private View view;
    private ScrollView register_page1;
    private CountryCodePicker ccp;
    private EditText et_phnNumber, et_code;
    private TextView tv_verify;
    private Button snd_btn, verify_btn, resend_btn, btn_proceed;

    private LoadToast mytoast;
    private boolean VERIFIED = false;


    public Phase1() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        callbackRegisterTo=(CallbackRegisterTo) context;
        EventBus.getDefault().register(this);
        Log.i(TAG, "OnAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "OnCreateView");
        view = inflater.inflate(R.layout.fragment_signup_phase1, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "OnActivityCreated");
        init();
    }

    private void init() {
        mytoast=new LoadToast(context)
        .setProgressColor(Color.GREEN)
        .setTranslationY(1400)
                .setBorderWidthDp(3)
        .setBorderColor(Color.WHITE);

        register_page1 = view.findViewById(R.id.register_page1);
        ccp = view.findViewById(R.id.ccp);
        et_phnNumber = view.findViewById(R.id.et_phnNo);
        ccp.registerCarrierNumberEditText(et_phnNumber);
        tv_verify = view.findViewById(R.id.tv_verify);
        snd_btn = view.findViewById(R.id.btn_send);
        verify_btn = view.findViewById(R.id.btn_verify);
        resend_btn = view.findViewById(R.id.btn_resend);
        et_code = view.findViewById(R.id.et_code);
        btn_proceed = view.findViewById(R.id.btn_proceed);

        setListners();

    }

    private void setListners() {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(context, "Country Code " + ccp.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();

            }
        });
        et_phnNumber.setOnClickListener(this);
        snd_btn.setOnClickListener(this);
        verify_btn.setOnClickListener(this);
        resend_btn.setOnClickListener(this);
        btn_proceed.setOnClickListener(this);


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
                if (completeNewText.length() < 10)
                    snd_btn.setVisibility(View.GONE);

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
                    snd_btn.setVisibility(View.VISIBLE);
                    startUpdates();
                    hideKeyboard();
                    endUpdates();
                }
            }
        });

        et_code.addTextChangedListener(new TextValidation() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {

                String completeOldText = before + old + after;
                String completeNewText = before + aNew + after;
                if (completeNewText.length() >= 7) {
                    et_code.setError("Must be a 6 digit code");
                }
                if (completeNewText.length() == 6)
                    hideKeyboard();
                endUpdates();

            }
        });
    }

    private void startPhase2() {
        if (VERIFIED) {
            Phase2 phase2 = new Phase2();
            FragmentTransaction trans = getFragmentManager().beginTransaction();
            trans.replace(R.id.frag_content, phase2).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "OnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "OnPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "OnSavedInstanceState");
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_send:

                sendCode();
                return;
            case R.id.btn_verify:

                verifyCode();
                return;
            case R.id.btn_resend:
                resendCode();
                return;
            case R.id.btn_proceed:
                proceed();
                return;
        }
    }


    private void sendCode() {

        String fullnumber;
        String phoneNumber = et_phnNumber.getText().toString();
        String countryCode = ccp.getDefaultCountryCodeWithPlus();
        fullnumber = countryCode + phoneNumber;
        boolean isDigitsOnly = TextUtils.isDigitsOnly(phoneNumber);
        if (isDigitsOnly) {
            if (!TextUtils.isEmpty(phoneNumber)) {
                mytoast.setText("Sending Code");
                mytoast.show();
                callbackRegisterTo.sendPhoneDetailsForVerify(fullnumber, countryCode);

            } else {
                Log.d(TAG, "ET_Phone Field is Empty: " + et_phnNumber.getText().toString());
                et_phnNumber.setError("Field is Required.");
            }
        } else {
            et_phnNumber.setError("Field is invalid.");
        }


    }


    private void verifyCode() {
        String code = et_code.getText().toString();
        boolean isDigitsOnly = TextUtils.isDigitsOnly(code);
        if (isDigitsOnly) {
            if (!TextUtils.isEmpty(code)) {
                mytoast.setText("Verifying Code");
                mytoast.show();
                callbackRegisterTo.verifySentCode(code);
            } else {
                Log.d(TAG, "ET_CODE Field is Empty: " + et_code.getText().toString());
                et_code.setError("Field is Required.");

            }
        } else
            et_code.setError("Field is Invalid.");

    }

    private void resendCode() {
    }

    private void proceed() {
        mytoast.setText("Proceeding..");
        mytoast.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        mytoast.hide();
                        startPhase2();
                    }
                }, 1500);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final UpdateUI state) {

        final int STATE_CODE_SENT = 2;
        final int STATE_NUMBER_INVALID = 3;
        final int STATE_VERIFY_SUCCESS = 4;
        final int STATE_ALREADY_VERIFIED = -4;
        final int STATE_SIGNIN_FAILED = 5;
        final int STATE_SIGNIN_SUCCESS = 6;
        final int STATE_NETWORK_LOSS=-88;
        Log.d(TAG, "State Code: " + state.getState() + "->" + state.getmsg());
        switch (state.getState()) {

            case STATE_NETWORK_LOSS:
                mytoast.error();
                callbackRegisterTo.showNetworkAlert();
                return;
            case STATE_NUMBER_INVALID:
                mytoast.hide();
                mytoast.setText("Invalid Number");
                mytoast.show();
                mytoast.error();
                return;

            case STATE_ALREADY_VERIFIED:
                mytoast.hide();
                mytoast.setText("Already Verified");
                mytoast.show();
                mytoast.success();
                return;

            case STATE_VERIFY_SUCCESS:
                mytoast.success();
                snd_btn.setEnabled(false);
                snd_btn.setBackgroundColor(Color.LTGRAY);
                snd_btn.setTextColor(context.getResources().getColor(R.color.colorAccent));
                return;

            case STATE_CODE_SENT:
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mytoast.success();
                                et_code.setVisibility(View.VISIBLE);
                                verify_btn.setVisibility(View.VISIBLE);
                                LinearLayout layout_btn = view.findViewById(R.id.layoutPanel_btn);
                                Animation righttoleft = AnimationUtils.loadAnimation(context, R.anim.righttoleft);
                                layout_btn.setAnimation(righttoleft);
                            }
                        }, 1500);
                return;
            case STATE_SIGNIN_SUCCESS:
                VERIFIED = true;
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mytoast.success();
                                btn_proceed.setVisibility(View.VISIBLE);
                                tv_verify.setVisibility(View.VISIBLE);
                                snd_btn.setVisibility(View.GONE);
                                et_code.setVisibility(View.GONE);
                                resend_btn.setVisibility(View.GONE);
                                verify_btn.setVisibility(View.GONE);
                            }
                        }, 1000);

                return;
            case STATE_SIGNIN_FAILED:
                mytoast.error();
                et_code.setError("PhoneSignIn Failed");
                resend_btn.setVisibility(View.VISIBLE);
                return;


        }
    }

    public void showAlerter(){
        Alerter.create(getActivity())
                .setText("No Internet Connectivity")
                .setBackgroundColorRes(R.color.color_error)
                .setDuration(1500)
                .setIcon(R.mipmap.wifi_alert)
                .show();
    }

    public void onStop() {
        super.onStop();
        Log.i(TAG, "OnStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "OnDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "OnDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        Log.i(TAG, "OnDetach");
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getActivity().getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }


}



