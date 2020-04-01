package com.example.izobonga_waiting_app.view;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.example.izobonga_waiting_app.model.WaitingData;
import com.example.izobonga_waiting_app.service.CallService;

import java.util.ArrayList;

//로컬영역 추가
public class CallActivity extends BaseActivity implements CallActivityView {
    private final String TAG = CallActivity.class.getName();
    RecyclerView recyclerView;
    CallAdapter adapter;
    ArrayList<Customer> customers;
    ArrayList<String> queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

//        customers = new ArrayList<>();
        queue = new ArrayList<>();
//        initRecyclerView();
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
//                customers.get(position).getTicket();
                showProgressDialog();
                tryCallCustomer(queue.get(position), position); //docID, index
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

    private void tryAddCustomer(final String docID) {
//        showProgressDialog();
        CallService callService = new CallService(this);
        callService.addWaitingCustomer(docID);
    }

    private void tryDeleteCustomer(final String docID){
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.deleteCustomer(docID);
    }

    @Override
    public void validateSuccess_initWaitingCustomer(ArrayList<Customer> customers) {
        this.customers = customers;
        initRecyclerView();
        printLog(TAG, "size: " + String.valueOf(customers.size()));

//        adapter.notifyDataSetChanged();
        hideProgressDialog();
    }

    @Override
    public void validateFailure(String message) {

    }

    @Override
    public void validateSuccess_addWaitingCustomer(Customer customer, String docID) {
        adapter.addItem(customer);
        queue.add(docID);
        adapter.notifyDataSetChanged();
        printLog(TAG, docID);
        printLog(TAG, customer.getPhone());
    }

    //고객 호출 성공했을 시, db 고객대기큐 에서 삭제됨.
    @Override
    public void validateSuccess_deleteWaitingCustomer(String docID, int position) {
        //호출 버튼 클릭 시 db에서 데이터 성공적으로 삭제되면 호출됨.
        queue.remove(docID);
        adapter.removeItem(position);
        adapter.notifyDataSetChanged();
        hideProgressDialog();

    }

    @Override
    public void added(WaitingData waitingData) {
        //초기화 시 호출. queue 에 고객의 docID가 들어있고 docID로 CUSTOMER 의 문서들을 읽어서 데이터 세팅해줌.
        printLog("added ", "added");
        queue = waitingData.getQueue();
    }

    @Override
    public void modified(WaitingData waitingData) {
        printLog("modified", "modify");
        //데이터 추가(변경)되거나
        int lastIndex = waitingData.getQueue().size() - 1;
        if ((lastIndex + 1) > queue.size()) { // 초기화 시 current data 안 가져오게 하기 위함.
            tryAddCustomer(waitingData.getQueue().get(lastIndex));
        }
    }

    @Override
    public void removed(WaitingData waitingData) {
        printLog("remove", "remove");
        int lastIndex1 = waitingData.getQueue().size() - 1;
        if ((lastIndex1 + 1) == waitingData.getTicket()) { // 초기화 시 current data 안 가져오게 하기 위함.
            queue = waitingData.getQueue();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //이벤트 리스너 해제

        CallService callService = new CallService(this);
        callService.detachListener();
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
                for (int i=0; i<queue.size(); i++){
                    tryDeleteCustomer(queue.get(i));
                }
                //init
                //ui update
                hideProgressDialog();
                break;
        }

        toast.show();

        return super.onOptionsItemSelected(item);
    }
}
