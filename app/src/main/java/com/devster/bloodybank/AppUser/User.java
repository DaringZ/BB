package com.devster.bloodybank.AppUser;

/**
 * Created by MOD on 5/6/2018.
 */

public class User {

    private static String name,email,phoneNumberWCode,bloodType,countryCode;
    private static int age;
    private static double lat,lng;
    private static boolean adult;



    public User(String name, String email, String bloodType, int age, boolean adult, double latitude, double longitude) {
        User.name=name;
        User.email=email;
        User.bloodType=bloodType;
        User.age=age;
        User.lat=latitude;
        User.lng=longitude;
        User.adult=adult;

    }

    public static void setPhoneNumberWCode(String phoneNumberWCode){
        User.phoneNumberWCode=phoneNumberWCode;
    }
    public static void setCountryCode(String countryCode){
        User.countryCode=countryCode;
    }
    public int getAge() {
        return age;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
    public String getName(){return name;}
    public String getEmail(){return email;}

    public String getPhoneNumberWCode() {
        return phoneNumberWCode;
    }

    public String getCountryCode() {
        return countryCode;
    }


    public String getBloodType() {
        return bloodType;
    }

    public boolean isAdult() {
        return adult;
    }

}
