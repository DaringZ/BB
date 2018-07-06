package com.devster.bloodybank.Views.Phases.Portrait;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.devster.bloodybank.Helpers.Interfaces.CallbackRegisterTo;
import com.devster.bloodybank.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class Phase2 extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private static final String TAG=Phase2.class.getSimpleName();
    protected CallbackRegisterTo callbackRegisterTo;
    final private int REQ_PICK_LOCATION=500;

    private RelativeLayout SIGNUP_LAYOUT;
    private Context context;
    private View view;

    TextView tv_error;
    EditText et_fullname,et_email,et_age;
    RadioGroup rg_gender;
    private Spinner spinner_bloodType;
    private SpinnerAdapter spinnerAdapter_BT;
    private Button btn_proocedToregister,btn_picLocation;

    private LoadToast mytoast;


    private String _name,_bloodType,_email,_gender;
    private String[] bloodGroups;
    private int _age;
    private double latitute,longitude;
    private String city="",country="";
    private boolean _adult;





    public Phase2() {
        // Required empty public constructor

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        callbackRegisterTo=(CallbackRegisterTo) context;
        Log.i(TAG,"OnAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"OnCreateView");
        view=inflater.inflate(R.layout.fragment_signup_phase2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"OnViewCreated");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"OnActivityCreated");
        init();


    }


    private void init() {

        mytoast=new LoadToast(context)
                .setProgressColor(Color.GREEN)
                .setTranslationY(500)
                .setBorderColor(Color.WHITE);
        hideKeyboard();

        SIGNUP_LAYOUT=view.findViewById(R.id.register_page2);

        tv_error=view.findViewById(R.id.tv_error);
        et_fullname=view.findViewById(R.id.et_fullName);
        et_email=view.findViewById(R.id.et_email);
        et_age=view.findViewById(R.id.et_age);
        bloodGroups=getResources().getStringArray(R.array.bloodType);
        spinner_bloodType=view.findViewById(R.id.spinner_BT);
        rg_gender=view.findViewById(R.id.rg_gender);
        btn_proocedToregister=view.findViewById(R.id.btn_proceedToregister);
        btn_picLocation=view.findViewById(R.id.btn_pickLocation);
        setSpinnerAdapter();
        setListeners();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"OnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"OnPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"OnSavedInstanceState");
    }

    private void setListeners() {

        spinner_bloodType.setOnItemSelectedListener(this);
        btn_proocedToregister.setOnClickListener(this);
        btn_picLocation.setOnClickListener(this);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)view.findViewById(checkedId);
                set_gender(rb.getText().toString());
            }
        });

    }


    private void setSpinnerAdapter() {
        spinnerAdapter_BT=new com.devster.bloodybank.Helpers.Adapters.SpinnerAdapter(context,bloodGroups);
        spinner_bloodType.setAdapter(spinnerAdapter_BT);
        spinner_bloodType.setDropDownVerticalOffset(150);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinner_Id=parent.getId();
        String selectedItemText;
        switch(spinner_Id){
            case R.id.spinner_BT:

                if(position>0){
                    selectedItemText = (String) parent.getItemAtPosition(position);
                    setBloodType(selectedItemText);
                }
                return;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btn_proceedToregister:

                if(callbackRegisterTo.isNetworkAvailable()){
                    if(isValidate()) {

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        callbackRegisterTo.sendUserDetailsForRegistering(_name,_bloodType,_age,_adult,_gender,_email,latitute,longitude,city,country);

                                    }
                                }, 1200);
                    }

                }
                return;

            case R.id.btn_pickLocation:
                pickLocation();
                break;
        }
    }


    private void pickLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), REQ_PICK_LOCATION);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQ_PICK_LOCATION:
                    Place place = PlacePicker.getPlace(context, data);

                    Snackbar.make(SIGNUP_LAYOUT,"Everything is Good to Go",Snackbar.LENGTH_LONG).show();

                    setLatitute(place.getLatLng().latitude);
                    setLongitude(place.getLatLng().longitude);
                    setAddress();
                    btn_picLocation.setEnabled(false);
                    btn_picLocation.setVisibility(View.INVISIBLE);
                    btn_proocedToregister.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try{

            addresses = geocoder.getFromLocation(latitute, longitude, 1);
            if(addresses!=null){
                Log.d(TAG,"Addresss found.");
                String city = addresses.get(0).getLocality();
                String country=addresses.get(0).getCountryName();
                setCityandCountry(city,country);

            }else{
                Log.d(TAG,"No addresss found.");
            }
        }
        catch(Exception e){

            e.printStackTrace();
            Log.w(TAG, "Canont get Address-> city and country !");
        }

    }
    private void setCityandCountry(String city, String country) {
        this.city=city;
        this.country=country;
    }

    private boolean isValidate() {

        boolean valid=true;
        set_name(et_fullname.getText().toString());
        set_email(et_email.getText().toString());
        set_age(Integer.parseInt(et_age.getText().toString()));
        if (!TextUtils.isEmpty(_name)) {
            if (_name.length() < 5) {
                et_fullname.setError("too small");
                valid=false;
            }else et_fullname.setError(null);
        }
        else {
            et_fullname.setError("Field is Required");
            valid = false;
        }
        if (!TextUtils.isEmpty(_email)){
            if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
                et_email.setError("invalid email address");
                valid=false;
            }else et_email.setError(null);

        }
        else {
            et_email.setError("Field is Required");
            valid=false;
        }
        if(TextUtils.isEmpty(_bloodType)){
            TextView error=(TextView) spinner_bloodType.getSelectedView();
            error.setError("Field is Required");
            valid=false;
        }
        if(TextUtils.isEmpty(_gender)){
            tv_error.setVisibility(View.VISIBLE);
            valid=false;
        }
        if(_age!=Math.round(_age)){
            et_age.setError("Age is invalid.");
            valid=false;
        }
        else if(_age>=18){
            set_adult(true);
        }
        return valid;

    }

    private void setBloodType(String selectedItemText) {
        this._bloodType=selectedItemText;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public void set_adult(boolean isAdult) {
        this._adult = isAdult;
    }
    public void set_name(String _name) {
        this._name = _name;
    }


    public void set_email(String _email) {
        this._email = _email;
    }

    public void set_age(int _age) {
        this._age = _age;
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getActivity().getCurrentFocus();
        if(focusedView!=null){
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public void setLatitute(double latitute) {
        this.latitute = latitute;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"OnStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"OnDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"OnDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG,"OnDetach");
    }


}
