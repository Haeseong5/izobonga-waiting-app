package com.example.izobonga_waiting_app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.izobonga_waiting_app.model.Customer;
import com.example.izobonga_waiting_app.model.WaitingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseApi {
    private String TAG = FirebaseApi.class.getName();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void increaseWaitingCount(final Timestamp time, final String phone, final int personnel) {
        DocumentReference customerRef = db.collection("manager").document("waiting");
        customerRef
                .update("ticket", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
                getTicket(time, phone, personnel);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }

    public void getTicket(final Timestamp time, final String phone, final int personnel){
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
                        addData(time, phone, personnel, ticket);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    //고객 정보 등록
    public void addData(Timestamp time, String phone, int personnel, int waitingNumber) {
        Customer customer = new Customer(time, phone, waitingNumber, personnel);
        db.collection("customer")
                .add(customer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,
                                "DocumentSnapshot written with ID: " + documentReference.getId());
                        pushWaitingQueue(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void pushWaitingQueue(String docID) {
        DocumentReference managerRef = db.collection("manager").document("waiting");
        managerRef
                .update("queue", FieldValue.arrayUnion(docID))
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

}
