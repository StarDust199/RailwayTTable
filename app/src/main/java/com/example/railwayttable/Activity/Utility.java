package com.example.railwayttable.Activity;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    static void showToast(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

    }

    static CollectionReference getCollectionReference(){
        return FirebaseFirestore.getInstance().collection("planner");
    }
    static String timeStampToString(Timestamp timestamp){
      return new SimpleDateFormat("dd MM YYYY").format(timestamp.toDate());

    }
}
