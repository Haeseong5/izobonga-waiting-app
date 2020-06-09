package com.example.izobonga_waiting_app;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FireBaseApi {
    private String TAG = FireBaseApi.class.getName();
    public static FirebaseFirestore db;
    public final static String COLLECTION_CUSTOMER = "customer";
    public final static String COLLECTION_MANAGER = "manager";
    public final static String COLLECTION_CALL = "call";

    public static FirebaseFirestore getInstance() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.KOREA);
}
