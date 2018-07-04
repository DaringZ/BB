package com.devster.bloodybank.Models;

/**
 * Created by MOD on 6/30/2018.
 */

public class UserDetails {



    private String id;
    private  String name;
    private String email;
    private String bloodType;

    private String gender;
    private String phoneNumberWCode;
    private String countryCode;
    private  int age;
    private  double lat,lng;
    private  boolean adult;
    private String city;
    private String country;



    public UserDetails(){}
    //Getters 14
    public String getId() {
        return id;
    }
    public String getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }
    public String getGender() {
        return gender;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getBloodType() {
        return bloodType;
    }
    public String getPhoneNumberWCode() {
        return phoneNumberWCode;
    }
    public String getCountryCode() {
        return countryCode;
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
    public boolean isAdult() {
        return adult;
    }




    //Setters 14
    public void setId(String id) {
        this.id = id;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    public void setPhoneNumberWCode(String phoneNumberWCode) {
        this.phoneNumberWCode = phoneNumberWCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public void setAdult(boolean adult) {
        this.adult = adult;
    }
}


