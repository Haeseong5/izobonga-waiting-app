package com.example.izobonga_waiting_app.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.izobonga_waiting_app.FireBaseApi;
import com.example.izobonga_waiting_app.interfaces.CallActivityView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CallService {
    private final String TAG = "CallService";
    private CallActivityView mCallActivityView;

    public CallService(final CallActivityView callActivityView) {
        this.mCallActivityView = callActivityView;
    }

    //웨이팅 고객 초기화. onCreate()에서 호출됨.
    public void initWaitingCustomer() {
        final ArrayList <Customer> customers = new ArrayList<>();
        FireBaseApi.getInstance().collection("customer").orderBy("waitingNumber", Query.Direction.ASCENDING)
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
                            mCallActivityView.validateSuccess_initWaitingCustomer(customers);
                        } else {
                            Log.d("initWaitingCustomer", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //대기고객 추가
    public void addWaitingCustomer(final String docID) {
        DocumentReference docRef = FireBaseApi.getInstance().collection("customer").document(docID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("addWaitingCustomer", "DocumentSnapshot data: " + document.getData());
                        Customer customer = document.toObject(Customer.class);
                        mCallActivityView.validateSuccess_addWaitingCustomer(customer, docID);
//                        adapter.addItem(customer);
//                        queue.add(docID);
//                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("addWaitingCustomer", "No such document");
                    }
                } else {
                    Log.d("addWaitingCustomer", "get failed with ", task.getException());
                }
            }
        });
    }

    //대기 고객 호출 버튼 클릭시 호출
    public void deleteWaitingCustomer(final String docID, final int position) {
//        showProgressDialog();
        FireBaseApi.getInstance().collection("customer").document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteWaitingCustomer", "DocumentSnapshot successfully deleted!");
                        removeInQueue(docID, position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteWaitingCustomer", "Error deleting document", e);
                    }
                });
    }

    public void removeInQueue(final String docID, final int position) {
        DocumentReference docRef = FireBaseApi.getInstance().collection("manager").document("waiting");
// Remove the 'capital' field from the document
        docRef
                .update("queue", FieldValue.arrayRemove(docID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("removeInQueue", "DocumentSnapshot successfully updated!");
                        mCallActivityView.validateSuccess_deleteWaitingCustomer(docID, position);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("removeInQueue", "Error updating document", e);
                    }
                });
    }

    //실시간 데이터 업데이트 리스너. 웨이팅 고객이 등록되거나 삭제될 때 콜백으로 작동함.
    public void setWaitingEventListener(){
        final String TAG = "setWaitingEventListener";
        FireBaseApi.getInstance().collection("manager")
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
                                    WaitingData addedWaitingData =  dc.getDocument().toObject(WaitingData.class);
                                    mCallActivityView.added(addedWaitingData);
                                    break;
                                case MODIFIED: //웨이팅 신청했을 때 호출됨.
                                    Log.d(TAG, "Modified CUSTOMER: " + dc.getDocument().getData());
                                    //초기화 작업 후 추가된 데이터까지 호출됨.
                                    WaitingData modifiedWaitingData =  dc.getDocument().toObject(WaitingData.class);
                                    mCallActivityView.modified(modifiedWaitingData);
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed CUSTOMER: " + dc.getDocument().getData());
                                    WaitingData removed =  dc.getDocument().toObject(WaitingData.class);
                                    mCallActivityView.removed(removed);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    public void detachListener() {
        // [START detach_listener]
        Query query = FireBaseApi.getInstance().collection("manager");
        ListenerRegistration registration = query.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    // [START_EXCLUDE]
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {

                    }
                    // [END_EXCLUDE]
                });
        // Stop listening to changes
        registration.remove();
        // [END detach_listener]
    }
}
