package com.example.izobonga_waiting_app.service;

import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.izobonga_waiting_app.FireBaseApi;
import com.example.izobonga_waiting_app.interfaces.CallActivityView;
import com.example.izobonga_waiting_app.model.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.izobonga_waiting_app.FireBaseApi.COLLECTION_CUSTOMER;
import static com.example.izobonga_waiting_app.FireBaseApi.COLLECTION_MANAGER;

public class CallService {
    private final String TAG = "CallService";
    private CallActivityView mCallActivityView;

    public CallService(final CallActivityView callActivityView) {
        this.mCallActivityView = callActivityView;
    }

    //웨이팅 고객 초기화. onCreate()에서 호출됨.
    public void initWaitingCustomer() {
        final ArrayList <Customer> customers = new ArrayList<>();
        FireBaseApi.getInstance().collection(COLLECTION_CUSTOMER).orderBy("ticket", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Customer customer = document.toObject(Customer.class);
                                customers.add(customer);
                            }
                            mCallActivityView.initCustomers(customers);
                        } else {
                            Log.d("initWaitingCustomer", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    //대기 고객 호출 버튼 클릭시 호출
    public void deleteWaitingCustomer(final String docID, final int position) {
//        showProgressDialog();
        FireBaseApi.getInstance().collection(COLLECTION_CUSTOMER).document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteWaitingCustomer", "DocumentSnapshot successfully deleted!");
                        mCallActivityView.removed(position);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteWaitingCustomer", "Error deleting document", e);
                    }
                });
    }

    public void setWaitingEventListener(){
        final String TAG = "setWaitingEventListener";
        FireBaseApi.getInstance().collection(COLLECTION_CUSTOMER)
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
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified CUSTOMER: " + dc.getDocument().getData());
                                    Customer customer = dc.getDocument().toObject(Customer.class);
                                    mCallActivityView.added(customer);
                                    break;
                                case REMOVED:
//                                    Log.d(TAG, "Removed CUSTOMER: " + dc.getDocument().getData());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    //manager컬렉션의 waiting, queue 필드 초기화.
    public void resetTicket(){
        DocumentReference docRef = FireBaseApi.getInstance().collection(COLLECTION_MANAGER).document("waiting");

        Map<String,Object> updates = new HashMap<>();
        updates.put("ticket", 0);

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            // [START_EXCLUDE]
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mCallActivityView.validateSuccessResetTicket(task.toString());
            }
            // [START_EXCLUDE]
        });
    }

    //대기고객 전체삭제/

    //티켓 번호만 초기화.
    //destroy 될 때 리스너 해제.


    public void sendSMS(String phoneNumber, String message){
        try {
            //전송
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            mCallActivityView.validateSuccessSMS("메세지 전송 성공");
        } catch (Exception e) {
            e.printStackTrace();
            mCallActivityView.validateFailureSMS("메세지 전송 실패. 없는 번호 입니다.");

        }
    }
}
