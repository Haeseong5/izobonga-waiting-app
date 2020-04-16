package com.example.izobonga_waiting_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClickListener(View view) {
        super.onClickListener(view);
        switch (view.getId()){
            case R.id.main_call_button:
                startActivity(new Intent(MainActivity.this, CallActivity.class));
                break;
            case R.id.main_customer_button:
                startActivity(new Intent(MainActivity.this, WaitingActivity.class));
                break;
            case R.id.main_manage_button:
                startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                break;
            default:
                break;
        }
    }
}
