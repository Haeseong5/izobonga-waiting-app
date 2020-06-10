package com.example.izobonga_waiting_app.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.izobonga_waiting_app.FireBaseHelper;
import com.example.izobonga_waiting_app.interfaces.WaitingActivityView;
import com.example.izobonga_waiting_app.models.Customer;
import com.example.izobonga_waiting_app.models.Ticket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.izobonga_waiting_app.FireBaseHelper.COLLECTION_CUSTOMER;
import static com.example.izobonga_waiting_app.FireBaseHelper.COLLECTION_CALL;
import static com.example.izobonga_waiting_app.FireBaseHelper.COLLECTION_MANAGER;

public class WaitingService {
    private final String TAG = "WaitingService";
    private final WaitingActivityView mWaitingActivityView;
    private ArrayList<String> mTicketList = new ArrayList<>(); //웨이팅 중인 고객 수 관리 하기 위한 List.

    public WaitingService(final WaitingActivityView waitingActivityView) {
        this.mWaitingActivityView = waitingActivityView;
    }


    public void increaseWaitingCount(final Timestamp time, final String phone, final int personnel, final int child) {
        FirebaseFirestore db = FireBaseHelper.getInstance();
        DocumentReference customerRef = db.collection(COLLECTION_CALL).document("waiting");
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

    public void getTicket(final Timestamp time, final String phone, final int personnel, final int child) {
        FirebaseFirestore db = FireBaseHelper.getInstance();
        DocumentReference docRef = db.collection(COLLECTION_CALL).document("waiting");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Ticket waitingData = document.toObject(Ticket.class);
                        int ticket = waitingData.getTicket();
                        addData(time, phone, personnel, ticket, child);
                        addCustomer(time, phone, personnel, child);
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
        Customer customer = new Customer();
        customer.setTimestamp(time);
        customer.setPhone(phone);
        customer.setChild(child);
        customer.setPersonnel(personnel);
        customer.setTicket(ticket);
        FireBaseHelper.getInstance().collection(COLLECTION_CUSTOMER).add(customer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,
                                "DocumentSnapshot written with ID: " + documentReference.getId());
                        setDocID(COLLECTION_CUSTOMER, documentReference.getId());
                        mWaitingActivityView.validateSuccess("success", ticket);
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

    //document 삭제를 용이하게 하기 위해 document 의 필드에 docID 등록
    public void setDocID(String collection, final String docID) {
        DocumentReference customerRef = FireBaseHelper.getInstance().collection(collection).document(docID);
        customerRef
                .update("docID", docID)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    //고객 정보 등록
    public void addCustomer(Timestamp time, String phone, int personnel, int child) {
        Customer customer = new Customer();
        customer.setTimestamp(time);
        customer.setPhone(phone);
        customer.setChild(child);
        customer.setPersonnel(personnel);
        FireBaseHelper.getInstance().collection(COLLECTION_MANAGER).add(customer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,
                                "DocumentSnapshot written with ID: " + documentReference.getId());
                        setDocID(COLLECTION_MANAGER, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void setWaitingEventListener() {
        final String TAG = "setWaitingEventListener";
        FireBaseHelper.getInstance().collection(COLLECTION_CUSTOMER)
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
                                case ADDED: //초기화 시 호출됨. + 고객 추가
                                    Log.d(TAG, "ADDDE CUSTOMER: " + dc.getDocument().getData());
                                    Customer addedCustomer = dc.getDocument().toObject(Customer.class);
                                    mTicketList.add(String.valueOf(addedCustomer.getTicket()));
                                    mWaitingActivityView.modified(mTicketList.size());
                                    break;
                                case REMOVED: //고객 호출되었을 때
                                    Log.d(TAG, "Removed CUSTOMER: " + dc.getDocument().getData());
                                    Customer removedCustomer = dc.getDocument().toObject(Customer.class);
                                    mTicketList.remove(String.valueOf(removedCustomer.getTicket())); // Value 으로 item 삭제하기 위해 ticket 을 String으로 변환하여 사용함.
                                    mWaitingActivityView.modified(mTicketList.size());
                                    mWaitingActivityView.speak(String.valueOf(removedCustomer.getTicket()));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

}
