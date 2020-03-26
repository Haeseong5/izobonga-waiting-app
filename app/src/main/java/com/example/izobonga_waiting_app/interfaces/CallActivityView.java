package com.example.izobonga_waiting_app.interfaces;

import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.model.WaitingData;

import java.util.ArrayList;

public interface CallActivityView {
    void validateSuccess_initWaitingCustomer(ArrayList<Customer> customers);
    void validateFailure(String message);

    void validateSuccess_addWaitingCustomer(Customer customer,String docID);
    void validateSuccess_deleteWaitingCustomer(String docID, int position);

    void added(WaitingData waitingData);
    void modified(WaitingData waitingData);
    void removed(WaitingData waitingData);
}
