package com.example.frup69513.cv.view;

import android.app.Application;
import android.util.Log;

import com.example.frup69513.cv.model.Skill;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 12/04/2017.
 */

public class App extends Application {

    private final static String TAG = "App";

    private List mPieEntries;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mPieEntries = new ArrayList();


        //get skill data from firebase database
        FirebaseDatabase.getInstance().getReference().child("competence").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("skill", dataSnapshot.toString());
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Skill skill = d.getValue(Skill.class);
                    Log.e("skill", skill.toString());
                    mPieEntries.add(new PieEntry(skill.getValue(), skill.getTitle()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List getPieEntries(){
        return mPieEntries;
    }
}
