package com.devster.bloodybank.Fragments.SignUpPhases.Portrait;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;
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

import com.devster.bloodybank.EventBuses.UpdateUI;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Registeration.Registration;
import com.devster.bloodybank.Registeration.TextValidation.Validation;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class Phase1 extends Fragment implements View.OnClickListener {

    private static final String TAG = Phase1.class.getSimpleName();
    private Context context;
    private View view;
    private ScrollView register_page1;
    private CountryCodePicker ccp;
    private EditText et_phnNumber, et_code;
    private TextView tv_verify;
    private Button snd_btn, verify_btn, resend_btn, btn_proceed;

    private ProgressDialog progressDialog;
    Registration register;

    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_CODE_FAILURE = 5;
    private static final int STATE_CODE_SUCCESS = 6;

    private String countryCode, phoneNumber, code;
    private String[] PhoneData;
    private boolean VERIFIED = false;


    public Phase1() {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "OnViewCreated");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "OnActivityCreated");
        init();
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

    private void init() {
        register = (Registration) getActivity();
        register_page1=view.findViewById(R.id.register_page1);
        ccp = view.findViewById(R.id.ccp);
        et_phnNumber = view.findViewById(R.id.et_phnNo);
        ccp.registerCarrierNumberEditText(et_phnNumber);
        setCountryCode(ccp.getDefaultCountryCodeWithPlus());
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


                Toast.makeText(context, "Updated " + ccp.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
                setCountryCode(ccp.getSelectedCountryCodeWithPlus());


            }
        });
        et_phnNumber.setOnClickListener(this);
        snd_btn.setOnClickListener(this);
        verify_btn.setOnClickListener(this);
        resend_btn.setOnClickListener(this);
        btn_proceed.setOnClickListener(this);


        et_phnNumber.addTextChangedListener(new Validation() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {

                String completeOldText = before + old + after;
                String completeNewText = before + aNew + after;

                if (completeNewText.length() == 1 && completeNewText.equals("0")) {
                    et_phnNumber.setText("");
                    et_phnNumber.setError("cannot start with 0");

                }
                if (completeNewText.equals(" ")) {
                    startUpdates();
                    et_phnNumber.setText("");
                    et_phnNumber.append(completeNewText.replaceAll("\\s", ""));
                    et_phnNumber.setError("no whitespaces");
                    endUpdates();
                }
                if (completeNewText.length() > 11) {
                    et_phnNumber.setError("Invalid Number");
                }
                if (completeNewText.length() == 10) {
                    snd_btn.setVisibility(View.VISIBLE);
                    startUpdates();
                    setPhoneNumber(et_phnNumber.getText().toString());
                    hideKeyboard();
                    endUpdates();
                }
            }
        });
        et_code.addTextChangedListener(new Validation() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {

                String completeOldText = before + old + after;
                String completeNewText = before + aNew + after;
                if (completeNewText.length() > 7) {
                    et_code.setError("Must be a 6 digit code");
                }
                if (completeNewText.length() == 6)
                    hideKeyboard();
                endUpdates();

            }
        });

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

        if (!TextUtils.isEmpty(phoneNumber)) {
            progressDialog = new ProgressDialog(context,
                    android.R.style.Theme_Black_NoTitleBar);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Sending...");
            progressDialog.show();
            Toast.makeText(context, "number " + countryCode + phoneNumber, Toast.LENGTH_SHORT).show();
            String fullNumber=countryCode+phoneNumber;
            register.verifyPhone(fullNumber,countryCode);
        } else
            et_phnNumber.setError("Field is empty.");
    }

    private void verifyCode() {
        if (!TextUtils.isEmpty(et_code.toString())) {
            progressDialog = new ProgressDialog(context,
                    android.R.style.Theme_Black_NoTitleBar);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Verifying..");
            progressDialog.show();
            code = et_code.getText().toString();

            register.verifyCode(code);
        } else
            et_code.setError("Field is empty.");

    }

    private void resendCode() {
    }

    private void proceed() {


        progressDialog = new ProgressDialog(context,
                android.R.style.ThemeOverlay_Material_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Prooceding..");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        startPhase2();
                    }
                }, 1500);
    }

    private void startPhase2() {
        if (VERIFIED) {
            PhoneData = new String[]{countryCode, countryCode+phoneNumber};
            Phase2 phase2 = new Phase2();
            Bundle bundle = new Bundle();
            final String KEY = "phase1";
            bundle.putStringArray(KEY, PhoneData);
            phase2.setArguments(bundle);
            FragmentTransaction trans = getFragmentManager().beginTransaction();
            trans.replace(R.id.frag_content, phase2).commit();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final UpdateUI state) {
        Toast.makeText(context, " State CODE" + state.getState()+state.getmsg(), Toast.LENGTH_SHORT).show();
        switch (state.getState()) {

            case STATE_VERIFY_FAILED:
                progressDialog.dismiss();
                Snackbar.make(register_page1,"Already Verified.LOGIN IN NOW",Snackbar.LENGTH_LONG).show();
                return;

            case STATE_VERIFY_SUCCESS:
                progressDialog.dismiss();
                Toast.makeText(context, " State CODE" + state.getState()+state.getmsg(), Toast.LENGTH_SHORT).show();
                snd_btn.setEnabled(false);
                snd_btn.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                return;

            case STATE_CODE_SENT:
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                et_code.setVisibility(View.VISIBLE);
                                verify_btn.setVisibility(View.VISIBLE);
                                LinearLayout layout_btn = view.findViewById(R.id.layoutPanel_btn);
                                Animation righttoleft = AnimationUtils.loadAnimation(context, R.anim.righttoleft);
                                layout_btn.setAnimation(righttoleft);
                            }
                        }, 1500);
                return;
            case STATE_CODE_SUCCESS:
                VERIFIED = true;
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                tv_verify.setVisibility(View.VISIBLE);
                                snd_btn.setVisibility(View.GONE);
                                et_code.setVisibility(View.GONE);
                                resend_btn.setVisibility(View.GONE);
                                verify_btn.setVisibility(View.GONE);
                            }
                        }, 1800);

                return;
            case STATE_CODE_FAILURE:
                progressDialog.dismiss();
                et_code.setError("Incorrect code");
                resend_btn.setVisibility(View.VISIBLE);
                return;

        }
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


    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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


}



