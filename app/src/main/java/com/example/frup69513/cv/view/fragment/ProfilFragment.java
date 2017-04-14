package com.example.frup69513.cv.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.frup69513.cv.R;
import com.example.frup69513.cv.model.Profil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilFragment extends Fragment {

    private final static String TAG = "ProfilFragment";

    private DatabaseReference mDatabase;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil,container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.civ_image_profil);

        mContext = getContext().getApplicationContext();
        mDatabase.child("profil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onCreateView():onDataChange()");

                Profil profil = dataSnapshot.getValue(Profil.class);

                Glide.with(mContext).load(profil.getPhotoUrl()).skipMemoryCache(true).into(circleImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCreateView():onCancelled():error: "+ databaseError);
            }
        });

        return v;
    }
}
