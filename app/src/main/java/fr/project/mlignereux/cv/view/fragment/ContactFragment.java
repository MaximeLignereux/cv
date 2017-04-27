package fr.project.mlignereux.cv.view.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.project.mlignereux.cv.model.Contact;

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
        final View v =  inflater.inflate(fr.project.mlignereux.cv.R.layout.fragment_contact, container, false);

        mFab = (FloatingActionButton) v.findViewById(fr.project.mlignereux.cv.R.id.fab);
        mFab1 = (FloatingActionButton) v.findViewById(fr.project.mlignereux.cv.R.id.fab1);
        mFab2 = (FloatingActionButton) v.findViewById(fr.project.mlignereux.cv.R.id.fab2);
        mCallTv = (TextView) v.findViewById(fr.project.mlignereux.cv.R.id.tv_call);
        mEmailTv = (TextView) v.findViewById(fr.project.mlignereux.cv.R.id.tv_email);

        fab_open = AnimationUtils.loadAnimation(mContext, fr.project.mlignereux.cv.R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, fr.project.mlignereux.cv.R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(mContext, fr.project.mlignereux.cv.R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(mContext, fr.project.mlignereux.cv.R.anim.rotate_backward);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getTitle());

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });

        final TextView stateTextView = (TextView)v.findViewById(fr.project.mlignereux.cv.R.id.tv_contact_state);
        final TextView bornDateTextView = (TextView)v.findViewById(fr.project.mlignereux.cv.R.id.tv_contact_born_date);
        final TextView mailTextView = (TextView)v.findViewById(fr.project.mlignereux.cv.R.id.tv_contact_mail);
        final TextView phoneTextView = (TextView)v.findViewById(fr.project.mlignereux.cv.R.id.tv_contact_phone);
        final TextView addressTextView = (TextView)v.findViewById(fr.project.mlignereux.cv.R.id.tv_contact_address);
        final TextView driversLicenseTextView = (TextView)v.findViewById(fr.project.mlignereux.cv.R.id.tv_contact_drivers_license);
        final TextView linkedinTextView = (TextView)v.findViewById(fr.project.mlignereux.cv.R.id.tv_contact_linkedin);
        final TextView viadeoTextView = (TextView)v.findViewById(fr.project.mlignereux.cv.R.id.tv_contact_viadeo);
        final ImageView viadeoImageView = (ImageView)v.findViewById(fr.project.mlignereux.cv.R.id.iv_contact_viadeo);
        final ImageView linkedinImageView = (ImageView)v.findViewById(fr.project.mlignereux.cv.R.id.iv_contact_linkedin);


        mDatabase.child(getReference()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Contact contact = dataSnapshot.getValue(Contact.class);

                stateTextView.setText(contact.getState());
                bornDateTextView.setText(contact.getBornDate());
                mailTextView.setText(contact.getEmail());
                phoneTextView.setText(contact.getPhone());
                addressTextView.setText(contact.getAddress());
                driversLicenseTextView.setText(contact.getDriversLicense());
                linkedinTextView.setText(contact.getLinkedin());

                viadeoTextView.setText(contact.getViadeo());


                Glide.with(mContext).load(contact.getLinkedinIcon()).skipMemoryCache( true ).into(linkedinImageView);
                Glide.with(mContext).load(contact.getViadeoIcon()).skipMemoryCache( true ).into(viadeoImageView);

                callNumber = contact.getPhone();

                call(callNumber);

                sendMail(contact.getEmail());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return v;
    }

    private void call(final String number){
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
                    callIntent.setData(Uri.parse("tel:" + number));
                    startActivity(callIntent);
                }


            }
        });
    }

    private void sendMail(final String email){

        mFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});

                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void animateFAB(){

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

        switch (requestCode) {

            case CALL_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + callNumber));
                    startActivity(callIntent);

                }
            }


        }
    }


}
