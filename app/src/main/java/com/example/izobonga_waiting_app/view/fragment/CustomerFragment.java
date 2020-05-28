package com.example.izobonga_waiting_app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.interfaces.ManageActivityView;
import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.service.ManageService;
import com.example.izobonga_waiting_app.view.CallActivity;
import com.example.izobonga_waiting_app.view.CallAdapter;
import com.example.izobonga_waiting_app.view.CustomerAdapter;
import com.example.izobonga_waiting_app.view.ManagerActivity;

import java.util.ArrayList;
import java.util.Objects;

public class CustomerFragment extends Fragment implements ManageActivityView {
    public static final String CUSTOMER_FRAGMENT = "CUSTOMER_FRAGMENT";
    private RecyclerView mRecyclerView;
    private ArrayList<Customer> customers;
    private CustomerAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customers = getArguments().getParcelableArrayList(CUSTOMER_FRAGMENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customer, container, false);
        mRecyclerView = rootView.findViewById(R.id.customer_recycler_view);

        // layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // adapter
        mAdapter = new CustomerAdapter(customers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemClickListener(new CustomerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // TODO : 아이템 클릭 이벤트를 MainActivity에서 처리.
                ((ManagerActivity) Objects.requireNonNull(getActivity())).showProgressDialog();

                tryDeleteCustomer(customers.get(position).getDocID(), position); //docID, index
            }
        });

        return rootView;
    }

    private void tryDeleteCustomer(String docID, int position){
        ManageService manageService = new ManageService(this);
        manageService.deleteData(docID, position);
    }

    @Override
    public void validateSuccess(ArrayList<Customer> customers) {

    }

    @Override
    public void validateSuccessDelete(String message, int position) {
        Toast.makeText(getActivity(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
        mAdapter.removeItem(position);
        mAdapter.notifyDataSetChanged();
        ((ManagerActivity) Objects.requireNonNull(getActivity())).hideProgressDialog();

    }

    @Override
    public void validateFailure(String message) {


    }
}
