package com.example.izobonga_waiting_app.view;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.telecom.Call;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.adapter.CallAdapter;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.interfaces.CallActivityView;
import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.model.Ticket;
import com.example.izobonga_waiting_app.service.CallService;

import java.util.ArrayList;

//로컬영역 추가
public class CallActivity extends BaseActivity implements CallActivityView {
    private final String TAG = CallActivity.class.getName();
    RecyclerView recyclerView;
    CallAdapter adapter;
    ArrayList<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        tryInitWaitingCustomer();
        setWaitingDataListener();
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
            public void onItemClick(View v, int position) {
                // TODO : 아이템 클릭 이벤트를 MainActivity에서 처리.
                printToast(String.valueOf(position));
                showProgressDialog();
                tryCallCustomer(customers.get(position).getDocID(), position); //docID, index
            }
        });
    }

    //데이터 변경 시 호출되는 리스너 등록
    private void setWaitingDataListener() {
        CallService callService = new CallService(this);
        callService.setWaitingEventListener();
    }

    //웨이팅 고객 초기화. onCreate()에서 호출됨.
    public void tryInitWaitingCustomer() {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.initWaitingCustomer();
    }

    //대기고객 호출하기 버튼 누르면 DB에서 아이템 제거 후 리스트 갱신.
    private void tryCallCustomer(final String docID, final int position) {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.deleteWaitingCustomer(docID, position);
    }


    @Override
    public void initCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
        initRecyclerView();
        adapter.notifyDataSetChanged();
        hideProgressDialog();
    }


    //고객이 추가되었을 때 호출 됨.
    @Override
    public void added(Customer customer) {
        printLog("modified", "modify");
        customers.add(customer);
        adapter.notifyDataSetChanged();
    }

    //고객이 삭제되었을 때 호출됨.
    @Override
    public void removed(int position) {
        adapter.removeItem(position);
        adapter.notifyDataSetChanged();
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
        Toast toast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG);
        switch(item.getItemId())
        {
            case R.id.menu_item_reset:
                hideProgressDialog();
                break;
        }
        toast.show();
        return super.onOptionsItemSelected(item);
    }
}
