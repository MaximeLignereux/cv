package com.example.frup69513.cv.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frup69513.cv.R;
import com.example.frup69513.cv.model.Data;
import com.example.frup69513.cv.model.Profil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilFragment extends Fragment {

    private final static String TAG = "ProfilFragment";

    private DatabaseReference mDatabase;

    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        final CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.civ_image_profil);

        mContext = view.getContext();

        mDatabase.child("profil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onCreateView():onDataChange()");

                Profil profil = dataSnapshot.getValue(Profil.class);

                Picasso.with(mContext).load(profil.getPhotoUrl()).into(circleImageView);
                Log.d(TAG,profil.getPhotoUrl());
                Log.d(TAG,profil.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCreateView():onCancelled():error: "+ databaseError);
            }
        });

        return view;
    }
}
