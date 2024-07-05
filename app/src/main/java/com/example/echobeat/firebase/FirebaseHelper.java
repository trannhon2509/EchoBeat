package com.example.echobeat.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper<T> {
    private static final String TAG = "FirebaseHelper";
    private FirebaseFirestore db;

    public FirebaseHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void addData(String collection, T data) {
        checkCollectionExists(collection, new CollectionExistsCallback() {
            @Override
            public void onCallback(boolean exists) {
                if (!exists) {
                    db.collection(collection)
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                } else {
                    Log.d(TAG, "Collection already exists. Data was not added.");
                }
            }
        });
    }

    public void getData(String collection, final Class<T> clazz, final DataCallback<T> callback) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<T> dataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                T item = document.toObject(clazz);
                                dataList.add(item);
                            }
                            callback.onCallback(dataList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            callback.onCallback(null);
                        }
                    }
                });
    }

    private void checkCollectionExists(String collection, CollectionExistsCallback callback) {
        db.collection(collection)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean exists = !task.getResult().isEmpty();
                            callback.onCallback(exists);
                        } else {
                            Log.w(TAG, "Error checking if collection exists.", task.getException());
                            callback.onCallback(false);
                        }
                    }
                });
    }

    public void getRecentData(String collection, String orderByField, int limit, final Class<T> clazz, final DataCallback<T> callback) {
        db.collection(collection)
                .orderBy(orderByField, Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<T> dataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                T item = document.toObject(clazz);
                                dataList.add(item);
                            }
                            callback.onCallback(dataList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            callback.onCallback(null);
                        }
                    }
                });
    }


    private interface CollectionExistsCallback {
        void onCallback(boolean exists);
    }

    public interface DataCallback<T> {
        void onCallback(List<T> data);
    }

}