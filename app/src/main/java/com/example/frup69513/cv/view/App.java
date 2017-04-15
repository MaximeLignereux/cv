package com.example.frup69513.cv.view;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by asus on 12/04/2017.
 */

public class App extends Application {

    private final static String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
