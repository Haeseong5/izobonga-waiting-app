package com.example.izobonga_waiting_app.interfaces;

import com.example.izobonga_waiting_app.model.Customer;

import java.util.ArrayList;

public interface ManageActivityView {
    void validateSuccess(ArrayList<Customer> customers);
    void validateSuccessDelete(String message, int position);

    void validateFailure(String message);

}
