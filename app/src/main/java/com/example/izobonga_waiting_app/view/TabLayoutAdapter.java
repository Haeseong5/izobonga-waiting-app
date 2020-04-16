package com.example.izobonga_waiting_app.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.view.fragment.CustomerFragment;

import java.util.ArrayList;

import static com.example.izobonga_waiting_app.view.fragment.CustomerFragment.CUSTOMER_FRAGMENT;


public class TabLayoutAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;
    private ArrayList<Customer> customers;

    public TabLayoutAdapter(FragmentManager fm, int tabCount, ArrayList<Customer> customers) {
        super(fm);
        this.tabCount = tabCount;
        this.customers = customers;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                CustomerFragment customerFragment = new CustomerFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(CUSTOMER_FRAGMENT, customers); // Key, Value
                customerFragment.setArguments(bundle);
                return customerFragment;
            case 1:
                CustomerFragment customerFragment2 = new CustomerFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList(CUSTOMER_FRAGMENT, customers); // Key, Value
                customerFragment2.setArguments(bundle2);
                return customerFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}


