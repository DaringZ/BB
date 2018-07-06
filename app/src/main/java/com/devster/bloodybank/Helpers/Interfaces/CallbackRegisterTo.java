package com.devster.bloodybank.Helpers.Interfaces;

/**
 * Created by MOD on 6/30/2018.
 */

public interface CallbackRegisterTo {
    void sendPhoneDetailsForVerify(String fullNumber,String countryCode);
    void sendUserDetailsForRegistering(String name, String bloodType, int age,boolean isAdult, String gender, String email, double lat, double lng,String city,String country);
    void verifySentCode(String code);
    boolean isNetworkAvailable();
    void showNetworkAlert();

}
