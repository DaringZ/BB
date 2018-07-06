package com.devster.bloodybank.Models;

/**
 * Created by MOD on 7/3/2018.
 */

public class RequestDetails {


    private String blood;
    private String atHospital;
    private String senderID;
    private String requestedTo;
    private int age;

    public RequestDetails(){}
    public RequestDetails(String senderID, String requestedTo, String blood, String atHospital, int age) {
        this.blood = blood;
        this.atHospital = atHospital;
        this.requestedTo = requestedTo;
        this.age = age;
        this.senderID=senderID;

    }

    //Getters
    public String getBlood() {
        return blood;
    }

    public String getAtHospital() {
        return atHospital;
    }

    public String getRequestedTo() {
        return requestedTo;
    }

    public int getAge() {
        return age;
    }

    public String getSenderID(){
        return senderID;
    }

}
