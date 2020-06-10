package com.example.izobonga_waiting_app.views.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.interfaces.ManageActivityView;
import com.example.izobonga_waiting_app.models.Customer;
import com.example.izobonga_waiting_app.services.ManageService;
import com.example.izobonga_waiting_app.views.adapters.TabLayoutAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ManagerActivity extends BaseActivity implements ManageActivityView {
    private ViewPager mViewPager;
    public TabLayout mTabLayout;
    private TextView mTvCustomerSize;
    private ArrayList<Customer> customers;
    private Toolbar mToolbar;
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
        mToolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(mToolbar);
        // Get the ActionBar here to configure the way it behaves.
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
            actionBar.setTitle(getString(R.string.manager_title));
        }


        mTvCustomerSize = findViewById(R.id.profile_tv_name);
        mTabLayout = findViewById(R.id.profile_tab_layout);
        mViewPager = findViewById(R.id.customer_view_pager);

        mTabLayout.addTab(mTabLayout.newTab().setText("전체 고객 조회"), 0);
        mTabLayout.addTab(mTabLayout.newTab().setText("고객 데이터 분석"), 1);
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

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home: //toolbar의 back버튼 눌렀을 때 동작
                finish();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
