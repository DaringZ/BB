package com.devster.bloodybank.Models;

/**
 * Created by MOD on 7/5/2018.
 */

public class Status {

    private String requesterByID;
    private boolean isrequested=false;
    private boolean isAccepted;
    private boolean isRecieved=false;


    public Status(String id,boolean requested){this.requesterByID=id;
    this.isrequested=requested;}

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        this.isAccepted = accepted;
    }

    public boolean isRecieved() {
        return isRecieved;
    }

    public void setRecieved(boolean recieved) {
        this.isRecieved = recieved;
    }

    public String getRequesterByID() {
        return requesterByID;
    }

    public void setRequesterByID(String requesterByID) {
        this.requesterByID = requesterByID;
    }

    public boolean isIsrequested() {
        return isrequested;
    }

    public void setIsrequested(boolean isrequested) {
        this.isrequested = isrequested;
    }
}

