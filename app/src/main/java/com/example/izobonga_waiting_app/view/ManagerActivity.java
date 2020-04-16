package com.example.izobonga_waiting_app.view;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.interfaces.ManageActivityView;
import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.service.ManageService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ManagerActivity extends BaseActivity implements ManageActivityView {
    private ViewPager mViewPager;
    public TabLayout mTabLayout;
    private TextView mTvCustomerSize, mTvEmail;
    private ArrayList<Customer> customers;
    private Toolbar toolbar;
//    private ArrayList<RecordResult> mRecordList;
    private TabLayoutAdapter mTapLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        initView();
        tryInitData();

    }

    private void initView() {
        toolbar = findViewById(R.id.call_toolbar);
        mTvCustomerSize = findViewById(R.id.profile_tv_name);
        mTvEmail = findViewById(R.id.profile_tv_email);
        mTabLayout = findViewById(R.id.profile_tab_layout);
        mViewPager = findViewById(R.id.customer_view_pager);

        mTabLayout.addTab(mTabLayout.newTab().setText("1"), 0);
        mTabLayout.addTab(mTabLayout.newTab().setText("2"), 1);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        // Set TabSelectedListener
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void tryInitData(){
        showProgressDialog();
        ManageService manageService = new ManageService(this);
        manageService.setData();
    }

    @Override
    public void validateSuccess(ArrayList<Customer> customers) {
        this.customers = customers;
        mTapLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager(), mTabLayout.getTabCount(), customers);
        mViewPager.setAdapter(mTapLayoutAdapter);
        hideProgressDialog();

    }

    @Override
    public void validateSuccessDelete(String message, int position) {

    }

    @Override
    public void validateFailure(String message) {

    }
}
