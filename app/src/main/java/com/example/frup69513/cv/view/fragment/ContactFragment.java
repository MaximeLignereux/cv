package com.example.frup69513.cv.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.frup69513.cv.R;
import com.example.frup69513.cv.model.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final static String TAG = "ContactFragment";

    private Boolean isFabOpen = false;
    private FloatingActionButton mFab, mFab1, mFab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private TextView mEmailTv, mCallTv;

    private DatabaseReference mDatabase;

    private Context mContext;

    private static final int CALL_PERMISSION = 1;
    private String callNumber;

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
                Log.d(TAG, "onCreateView():onDataChange():datasnaphot: " + dataSnapshot);

                final Contact contact = dataSnapshot.getValue(Contact.class);
                callNumber = contact.getPhone();

                mFab1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "checkpermission");

                            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)){
                                Log.d(TAG, "shouldRequest");

                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder.setMessage("Vous devez autoriser l'application à utiliser votre appareil pour téléphoner");

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
                                    }
                                });
                                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();

                                dialog.show();

                            }else{

                                // No explanation needed, we can request the permission.
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
                            }
                        }else{
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + callNumber));
                            startActivity(callIntent);
                        }


                    }
                });
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull  String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult");

        switch (requestCode) {

            case CALL_PERMISSION: {

                Log.d(TAG, "CALL_PERMISSION");

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "permissionResult");

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + callNumber));
                    startActivity(callIntent);

                }
                return;
            }


        }
    }


}
