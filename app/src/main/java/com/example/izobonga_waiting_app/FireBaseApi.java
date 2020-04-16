package com.example.izobonga_waiting_app;

import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseApi {
    private String TAG = FireBaseApi.class.getName();
    public static FirebaseFirestore db;
    public final static String COLLECTION_CUSTOMER = "customer";
    public final static String COLLECTION_MANAGER = "manager";
    public final static String COLLECTION_CALL = "call";
    public final static String FILED_TICKET = "ticket";



    public static FirebaseFirestore getInstance() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

}
