package com.example.izobonga_waiting_app.model;

import com.google.firebase.Timestamp;

public class Customer {
    private Timestamp timestamp;
    private String phone;
    private int waitingNumber;
    private int personnel;
    private int child;
    public Customer(){
    }

    public Customer(Timestamp timestamp, String phone, int waitingNumber, int personnel, int child) {
        this.timestamp = timestamp;
        this.phone = phone;
        this.waitingNumber = waitingNumber;
        this.personnel = personnel;
        this.child = child;
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

    public int getTicket() {
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

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }
}
