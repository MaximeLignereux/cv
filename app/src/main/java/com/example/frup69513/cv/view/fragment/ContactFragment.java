package com.example.frup69513.cv.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.frup69513.cv.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactFragment extends Fragment {

    private final static String TAG = "ContactFragment";

    private Boolean isFabOpen = false;
    private FloatingActionButton mFab, mFab1, mFab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private TextView mEmailTv, mCallTv;

    private DatabaseReference mDatabase;

    private Context mContext;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance(String reference, String title, String subtitle, String description) {
        Log.d(TAG,"newInstance():reference: " + reference);

        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString("REFERENCE", reference);
        args.putString("TITLE", title);
        args.putString("SUBTITLE", subtitle);
        args.putString("DESCRIPTION", description);
        fragment.setArguments(args);

        return fragment;
    }


    public String getReference(){
        return getArguments().getString("REFERENCE");
    }

    public String getTitle(){
        return getArguments().getString("TITLE");
    }

    public String getSubtitle() { return getArguments().getString("SUBTITLE");}

    public String getDescription() { return getArguments().getString("DESCRIPTION"); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext= getContext().getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_contact, container, false);

        mFab = (FloatingActionButton) v.findViewById(R.id.fab);
        mFab1 = (FloatingActionButton) v.findViewById(R.id.fab1);
        mFab2 = (FloatingActionButton) v.findViewById(R.id.fab2);
        mCallTv = (TextView) v.findViewById(R.id.tv_call);
        mEmailTv = (TextView) v.findViewById(R.id.tv_email);

        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext,R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(mContext,R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(mContext,R.anim.rotate_backward);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getTitle());

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });

        mDatabase.child(getReference()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    public void animateFAB(){

        if(isFabOpen){

            mFab.startAnimation(rotate_backward);
            mFab1.startAnimation(fab_close);
            mFab2.startAnimation(fab_close);
            mCallTv.startAnimation(fab_close);
            mEmailTv.startAnimation(fab_close);
            mFab1.setClickable(false);
            mFab2.setClickable(false);
            isFabOpen = false;

        } else {

            mFab.startAnimation(rotate_forward);
            mFab1.startAnimation(fab_open);
            mFab2.startAnimation(fab_open);
            mCallTv.startAnimation(fab_open);
            mEmailTv.startAnimation(fab_open);
            mFab1.setClickable(true);
            mFab2.setClickable(true);
            isFabOpen = true;

        }
    }


}
