package com.devster.bloodybank.Models;

/**
 * Created by MOD on 7/2/2018.
 */

public class Requests {

    private static int numOfRequests=0;
    private RequestDetails details;
    private final Status status=new Status();

    public Requests(RequestDetails details){
        numOfRequests+=1;
        this.details=details;
    }


    //Getters
    public static int getNumOfRequests() {
        return numOfRequests;
    }

    public RequestDetails getDetails() {
        return details;
    }

    //Setters
    public Status getStatus() {
        return status;
    }

    public void setDetails(RequestDetails details) {
        this.details = details;
    }




    private class Status{

        private boolean accepted=false;
        private boolean recieved=false;

        private Status(){}

        public boolean isAccepted() {
            return accepted;
        }

        public void setAccepted(boolean accepted) {
            this.accepted = accepted;
        }

        public boolean isRecieved() {
            return recieved;
        }

        public void setRecieved(boolean recieved) {
            this.recieved = recieved;
        }
    }
}
