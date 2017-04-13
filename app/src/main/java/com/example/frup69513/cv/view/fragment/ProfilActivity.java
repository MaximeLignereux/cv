package com.example.frup69513.cv.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frup69513.cv.R;
import com.example.frup69513.cv.model.Profil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity  implements AppBarLayout.OnOffsetChangedListener{

    private final static String TAG = "ProfilActivity";

    private DatabaseReference mDatabase;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;


    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final CircleImageView circleImageView = (CircleImageView) findViewById(R.id.civ_image_profil);

        mContext = getApplicationContext();
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

        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

    }

    private void bindActivity() {
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.app_bar);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();

    }



}
