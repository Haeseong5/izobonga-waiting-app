package com.example.izobonga_waiting_app.model;

import com.google.firebase.Timestamp;

public class Customer {
    private Timestamp timestamp;
    private String phone;
    private int waitingNumber;
    private int personnel;
    public Customer(){
    }
    public Customer(Timestamp timestamp, String phone, int waitingNumber, int personnel) {
        this.timestamp = timestamp;
        this.phone = phone;
        this.waitingNumber = waitingNumber;
        this.personnel = personnel;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getWaitingNumber() {
        return waitingNumber;
    }

    public void setWaitingNumber(int waitingNumber) {
        this.waitingNumber = waitingNumber;
    }

    public int getPersonnel() {
        return personnel;
    }

    public void setPersonnel(int personnel) {
        this.personnel = personnel;
    }
}
