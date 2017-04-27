package fr.project.mlignereux.cv.view;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by asus on 12/04/2017.
 */

public class App extends Application {

    private final static String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
