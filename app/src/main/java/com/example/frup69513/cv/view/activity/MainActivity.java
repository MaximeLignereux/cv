package com.example.frup69513.cv.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.frup69513.cv.R;
import com.example.frup69513.cv.model.Profil;
import com.example.frup69513.cv.view.fragment.SkillFragment;
import com.example.frup69513.cv.view.fragment.DataFragment;
import com.example.frup69513.cv.view.fragment.ProfilFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DataFragment.OnFragmentInteractionListener {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        final Context context = header.getContext();

        final TextView nameTextView = (TextView) header.findViewById(R.id.tv_text_header);
        final CircleImageView circleImageView = (CircleImageView) header.findViewById(R.id.civ_image_header);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("profil");
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profil profil = dataSnapshot.getValue(Profil.class);

                nameTextView.setText(profil.getName());
                Picasso.with(getApplication()).load(profil.getPhotoUrl()).into(circleImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        switch (id){
            case R.id.profil:
                fragment = new ProfilFragment();
                break;

            case R.id.nav_formation :
                fragment = DataFragment.newInstance("formation");
                break;

            case R.id.nav_studie_project:
                fragment = DataFragment.newInstance("projet_etude");
                break;

            case R.id.nav_vounteer_experience:
                fragment = DataFragment.newInstance("benevolat");
                break;

            case R.id.nav_professional_experience:
                fragment = DataFragment.newInstance("professionnelle");
                break;

            case R.id.nav_skill:
                fragment = new SkillFragment();
                break;

            case R.id.nav_hobbie:
                fragment = DataFragment.newInstance("hobbie");
                break;

            case R.id.nav_contact:
                fragment = DataFragment.newInstance("contact");
                break;

            case R.id.nav_info:
                fragment = DataFragment.newInstance("info");
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
