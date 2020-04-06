package com.example.izobonga_waiting_app.interfaces;

import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.model.Ticket;

import java.util.ArrayList;

public interface CallActivityView {
    void initCustomers(ArrayList<Customer> customers);
    void added(Customer customer);
    void removed(int position);
    void validateSuccessSMS(String message);
    void validateFailureSMS(String message);
    void validateSuccessResetTicket(String message);

}
