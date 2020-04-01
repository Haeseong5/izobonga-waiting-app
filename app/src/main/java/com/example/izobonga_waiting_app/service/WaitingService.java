package com.example.izobonga_waiting_app.service;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.izobonga_waiting_app.FireBaseApi;
import com.example.izobonga_waiting_app.interfaces.WaitingActivityView;
import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.model.WaitingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class WaitingService {
    private final String TAG = "WaitingService";
    private final WaitingActivityView mWaitingActivityView;

    public WaitingService(final WaitingActivityView waitingActivityView) {
        this.mWaitingActivityView = waitingActivityView;
    }



    public void increaseWaitingCount(final Timestamp time, final String phone, final int personnel, final int child) {
        FirebaseFirestore db = FireBaseApi.getInstance();
        DocumentReference customerRef = db.collection("manager").document("waiting");
        customerRef
                .update("ticket", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        getTicket(time, phone, personnel, child);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        mWaitingActivityView.validateFailure("failure increaseWaitingCount");
                    }
                });

    }

    public void getTicket(final Timestamp time, final String phone, final int personnel, final int child){
        FirebaseFirestore db = FireBaseApi.getInstance();
        DocumentReference docRef = db.collection("manager").document("waiting");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        WaitingData waitingData = document.toObject(WaitingData.class);
                        int ticket = waitingData.getTicket();
                        addData(time, phone, personnel, ticket, child);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    mWaitingActivityView.validateFailure("failure getTicket");
                }
            }
        });
    }

    //고객 정보 등록
    public void addData(Timestamp time, String phone, int personnel, final int ticket, int child) {
        FirebaseFirestore db = FireBaseApi.getInstance();
        Customer customer = new Customer();
        customer.setTimestamp(time);
        customer.setPhone(phone);
        customer.setChild(child);
        customer.setPersonnel(personnel);
        customer.setWaitingNumber(ticket);

        db.collection("customer")
                .add(customer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,
                                "DocumentSnapshot written with ID: " + documentReference.getId());
                        pushWaitingQueue(documentReference.getId(), ticket);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        mWaitingActivityView.validateFailure("failure addData");
                    }
                });
    }

    public void pushWaitingQueue(String docID, final int ticket) {
        FirebaseFirestore db = FireBaseApi.getInstance();
        DocumentReference managerRef = db.collection("manager").document("waiting");
        managerRef
                .update("queue", FieldValue.arrayUnion(docID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        mWaitingActivityView.validateSuccess("success", ticket);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        mWaitingActivityView.validateFailure("failure pushWaitingQueue");
                    }
                });
    }

    public void waitingListener(){
        final DocumentReference docRef = FireBaseApi.getInstance().collection("customer").document("waiting");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("setWaitingEventListener", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("setWaitingEventListener", "Current data: " + snapshot.getData());
//                    WaitingData waitingData = snapshot.toObject(WaitingData.class);
//                    int waiting = waitingData.getQueue().size();
//                    binding.waitingCountText.setText(waiting+"");
                } else {
                    Log.d("setWaitingEventListener", "Current data: null");
                }
            }
        });
    }
}
