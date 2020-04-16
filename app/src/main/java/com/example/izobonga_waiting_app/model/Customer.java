package com.example.izobonga_waiting_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Customer implements Parcelable {
    private Timestamp timestamp;
    private String phone;
    private int ticket;
    private int personnel;
    private int child;
    private String docID;
    public Customer(){
    }

    protected Customer(Parcel in) {
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        phone = in.readString();
        ticket = in.readInt();
        personnel = in.readInt();
        child = in.readInt();
        docID = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

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
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
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

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(timestamp, i);
        parcel.writeString(phone);
        parcel.writeInt(ticket);
        parcel.writeInt(personnel);
        parcel.writeInt(child);
        parcel.writeString(docID);
    }
}
