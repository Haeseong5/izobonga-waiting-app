package com.example.izobonga_waiting_app.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.FirebaseApi;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.adapter.ManagerAdapter;
import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.model.WaitingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
//로컬영역 추가
public class ManagerActivity extends BaseActivity {
    FirebaseApi firebaseApi;
    RecyclerView recyclerView;
    ManagerAdapter adapter;
    ArrayList<Customer> customers;
    ArrayList<String> queue;
    int mTicket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        firebaseApi = new FirebaseApi();
        customers = new ArrayList<>();
        queue = new ArrayList<>();
        initRecyclerView();
        initWaitingCustomer();
        waitingListener();
    }
    void initRecyclerView(){
        recyclerView = findViewById(R.id.manager_recyclerView);
        // layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        adapter = new ManagerAdapter( customers);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ManagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // TODO : 아이템 클릭 이벤트를 MainActivity에서 처리.
                printToast(String.valueOf(position));
//                customers.get(position).getWaitingNumber();

                callCustomer(queue.get(position), position);
            }
        }) ;
    }

    //웨이팅 고객 초기화. onCreate()에서 호출됨.
    public void initWaitingCustomer(){
        firebaseApi.db.collection("customer").orderBy("waitingNumber", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("initWaitingCustomer", document.getId() + " => " + document.getData());
                                Customer customer = document.toObject(Customer.class);
                                customers.add(customer);
//                                queue.add(document.getId());
                                mTicket = customer.getWaitingNumber();
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("initWaitingCustomer", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    //실시간 데이터 업데이트 리스너. 웨이팅 고객이 등록되거나 삭제될 때 콜백으로 작동함.
    public void waitingListener(){
        final String TAG = "waitingListener";
        firebaseApi.db.collection("manager")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED: //초기화시 호출
                                    Log.d(TAG, "ADDDE CUSTOMER: " + dc.getDocument().getData());
                                    WaitingData added =  dc.getDocument().toObject(WaitingData.class);
                                    queue = added.getQueue();
                                    Log.d("ADDED QUEUE SIZE", " " +queue.size());
                                    break;
                                case MODIFIED: //웨이팅 신청했을 때 호출됨.
                                    Log.d(TAG, "Modified CUSTOMER: " + dc.getDocument().getData());
                                    //초기화 작업 후 추가된 데이터까지 호출됨.
                                    WaitingData modified =  dc.getDocument().toObject(WaitingData.class);
                                    Log.d("MODIFIED QUEUE SIZE", " " +modified.getQueue().size());
                                    Log.d("MODIFIED QUEUE SIZE2", " " +queue.size());

                                    int lastIndex = modified.getQueue().size()-1;
                                    if((lastIndex+1) > queue.size()){ // 초기화 시 current data 안 가져오게 하기 위함.
                                        getWaitingCustomer(modified.getQueue().get(lastIndex));
                                        Log.d("log","get호출");
                                    }
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed CUSTOMER: " + dc.getDocument().getData());
                                    WaitingData removed =  dc.getDocument().toObject(WaitingData.class);
                                    int lastIndex1 = removed.getQueue().size()-1;
                                    if((lastIndex1+1) == removed.getTicket()){ // 초기화 시 current data 안 가져오게 하기 위함.
//                                        getWaitingCustomer(removed.getQueue().get(lastIndex1));
                                        queue = removed.getQueue();
//                                        queue = waitingData.getQueue();
                                    }
                                    adapter.notifyDataSetChanged();
//                                    queue = dc.getDocument().toObject(WaitingData.class).getQueue();
                                    break;
                            }
                        }

                    }
                });
    }

    public void getWaitingCustomer(final String docID){
        DocumentReference docRef = firebaseApi.db.collection("customer").document(docID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getWaitingCustomer", "DocumentSnapshot data: " + document.getData());
                        Customer customer = document.toObject(Customer.class);
                        adapter.addItem(customer);
//                        customers.add(customer);
                        queue.add(docID);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("getWaitingCustomer", "No such document");
                    }
                } else {
                    Log.d("getWaitingCustomer", "get failed with ", task.getException());
                }
            }
        });
    }
    public void callCustomer(final String docID, final int position){
        firebaseApi.db.collection("customer").document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("callCustomer", "DocumentSnapshot successfully deleted!");
                        removeInQueue(docID, position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("callCustomer", "Error deleting document", e);
                    }
                });
    }
    public void removeInQueue(final String docID, final int position){
        DocumentReference docRef = firebaseApi.db.collection("manager").document("waiting");
// Remove the 'capital' field from the document
        docRef
            .update("queue", FieldValue.arrayRemove(docID))
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("removeInQueue", "DocumentSnapshot successfully updated!");
                    queue.remove(docID);
                    adapter.removeItem(position);
                    adapter.notifyDataSetChanged();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("removeInQueue", "Error updating document", e);
                }
            });

    }
}
