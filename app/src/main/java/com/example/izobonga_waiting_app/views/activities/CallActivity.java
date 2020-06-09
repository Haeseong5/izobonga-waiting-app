package com.example.izobonga_waiting_app.views.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.interfaces.CallActivityView;
import com.example.izobonga_waiting_app.models.Customer;
import com.example.izobonga_waiting_app.services.CallService;
import com.example.izobonga_waiting_app.views.adapters.CallAdapter;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

//로컬영역 추가
public class CallActivity extends BaseActivity implements CallActivityView{
    private final String TAG = CallActivity.class.getName();
    MediaPlayer mMediaPlayer;
    RecyclerView recyclerView;
    TextView tvNoCustomerText;
    Toolbar mToolbar;
    CallAdapter adapter;
    ArrayList<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //상태바 없애기
        setContentView(R.layout.activity_call);
        initView();
        tryInitWaitingCustomer();
        setWaitingDataListener();
        if (mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(this, R.raw.sound_alarm_file);
        }
        checkPermission();
    }

    private void initView(){
        tvNoCustomerText = findViewById(R.id.call_tv_message);
        mToolbar = findViewById(R.id.call_toolbar);
        setSupportActionBar(mToolbar);

        // Get the ActionBar here to configure the way it behaves.
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
            actionBar.setTitle(getString(R.string.call_title));
        }
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.call_recyclerView);
        // layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        adapter = new CallAdapter(customers);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CallAdapter.OnItemClickListener() {

            @Override
            public void onItemClickCall(View v, int position) {
                showProgressDialog();
                tryCallCustomer(customers.get(position).getDocID(), position); //docID, index
            }

            @Override
            public void onItemClickDelete(View v, int position) {
                showProgressDialog();
                tryDelete(customers.get(position).getDocID(), position); //docID, index
            }
        });
    }

    public void setZeroDataMessage(){
        if (customers.size()==0){
            tvNoCustomerText.setVisibility(View.VISIBLE);
        }else{
            tvNoCustomerText.setVisibility(View.GONE);
        }
    }
    //웨이팅 고객 초기화. onCreate()에서 호출됨.
    public void tryInitWaitingCustomer() {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.initWaitingCustomer();
    }

    //데이터 변경 시 호출되는 리스너 등록
    private void setWaitingDataListener() {
        CallService callService = new CallService(this);
        callService.setWaitingEventListener();
    }

    //대기고객 호출하기 버튼 누르면 DB에서 아이템 제거 후 리스트 갱신.
    private void tryCallCustomer(final String docID, final int position) {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.callWaitingCustomer(docID, position);
    }
    //대기고객 호출하기 버튼 누르면 DB에서 아이템 제거 후 리스트 갱신.
    private void tryDelete(final String docID, final int position) {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.deleteCustomer(docID, position);
    }
    //대기고객 호출하기 버튼 누르면 DB에서 아이템 제거 후 리스트 갱신.
    private void tryResetTicket() {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.resetTicket();
    }

    private void tryCallSMS(String phoneNumber, String message, int ticket){
        CallService callService = new CallService(this);
        callService.sendSMS(phoneNumber, message, ticket);
    }

    private void tryAlarm(){
        if (mMediaPlayer != null){
            mMediaPlayer.start();
        }
    }

    private void checkPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("호출 메세지를 전송하기 위해 SMS전송 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.SEND_SMS)
                .check();
    }

    @Override
    public void initCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
        initRecyclerView();
        adapter.notifyDataSetChanged();
        setZeroDataMessage();
        hideProgressDialog();
    }


    //고객이 추가되었을 때 호출 됨.
    @Override
    public void added(Customer customer) {
        printLog("modified", "modify");
        tryAlarm();
        customers.add(customer);
        setZeroDataMessage();
        adapter.notifyDataSetChanged();
    }

    //고객이 삭제되었을 때 호출됨.
    @Override
    public void called(int position) {
        tryCallSMS(customers.get(position).getPhone(), getString(R.string.sms_message),customers.get(position).getTicket()); //문자메세지 전송
        adapter.removeItem(position);
        setZeroDataMessage();
        adapter.notifyDataSetChanged();
        hideProgressDialog();
    }

    @Override
    public void deleted(int position) {
        adapter.removeItem(position);
        setZeroDataMessage();
        adapter.notifyDataSetChanged();
        hideProgressDialog();
    }

    @Override
    public void validateSuccessSMS(String message) {
        printToast(message);
    }

    @Override
    public void validateFailureSMS(String message) {
        printToast(message);
    }

    @Override
    public void validateSuccessResetTicket(String message) {
        hideProgressDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_item_reset:
                tryResetTicket();
                break;
            case android.R.id.home: //toolbar의 back버튼 눌렀을 때 동작
                finish();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
//        mMediaPlayerService.remove();
        if (mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }
}
