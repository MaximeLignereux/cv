package fr.project.mlignereux.cv.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.project.mlignereux.cv.R;
import fr.project.mlignereux.cv.model.Profil;

public class ProfilFragment extends Fragment {

    private final static String TAG = "ProfilFragment";

    private Context mContext;

    public ProfilFragment(){}

    public static Fragment newInstance(String reference, String title) {

        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(String.valueOf(R.string.reference_bundle), reference);
        args.putString(String.valueOf(R.string.title_bundle), title);
        fragment.setArguments(args);

        return fragment;
    }

    public String getReference(){
        return getArguments().getString(getString(R.string.reference_bundle));
    }

    public String getTitle(){
        return getArguments().getString(getString(R.string.title_bundle));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(fr.project.mlignereux.cv.R.layout.fragment_profil,container, false);

        final CircleImageView circleImageView = (CircleImageView) v.findViewById(fr.project.mlignereux.cv.R.id.civ_image_profil);
        final ImageView imageViewTop = (ImageView) v.findViewById(fr.project.mlignereux.cv.R.id.image_view_top);
        final TextView profilNameTextView = (TextView) v.findViewById(fr.project.mlignereux.cv.R.id.profil_name);
        final TextView profilJobTextView = (TextView) v.findViewById(fr.project.mlignereux.cv.R.id.profil_job);
        final TextView descriptionTextView = (TextView) v.findViewById(fr.project.mlignereux.cv.R.id.tv_description_profil);
        final FloatingActionButton contactFab = (FloatingActionButton) v.findViewById(fr.project.mlignereux.cv.R.id.fab_contact);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mContext = getContext().getApplicationContext();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getTitle());

        mDatabase.child(getReference()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onCreateView():onDataChange()");

                Profil profil = dataSnapshot.getValue(Profil.class);

                Glide.with(mContext).load(profil.getPhotoUrl()).skipMemoryCache(true).into(circleImageView);
                Glide.with(mContext).load(profil.getBackgroundUrl()).skipMemoryCache(true).centerCrop().into(imageViewTop);
                profilJobTextView.setText(profil.getJob());
                profilNameTextView.setText(profil.getName());
                descriptionTextView.setText(profil.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCreateView():onCancelled():error: "+ databaseError);
            }
        });

        contactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(fr.project.mlignereux.cv.R.id.content_main, ContactFragment.newInstance("contact", "Contact", "", ""))
                        .commit();
            }
        });

        return v;
    }
}
