package fr.project.mlignereux.cv.view.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
import fr.project.mlignereux.cv.view.fragment.ContactFragment;
import fr.project.mlignereux.cv.view.fragment.DataFragment;
import fr.project.mlignereux.cv.view.fragment.ProfilFragment;
import fr.project.mlignereux.cv.view.fragment.SkillFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MainActivity";
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(fr.project.mlignereux.cv.R.layout.activity_main);

        if(mReference == null){
            mReference = FirebaseDatabase.getInstance().getReference();
            mReference.keepSynced(true);
        }


        final Toolbar toolbar = (Toolbar) findViewById(fr.project.mlignereux.cv.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(fr.project.mlignereux.cv.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, fr.project.mlignereux.cv.R.string.navigation_drawer_open, fr.project.mlignereux.cv.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, ProfilFragment.newInstance("profil","Profil"))
                .commit();

        View header = navigationView.getHeaderView(0);

        final TextView nameTextView = (TextView) header.findViewById(R.id.tv_text_header);
        final TextView mailTextView = (TextView) header.findViewById(R.id.tv_mail_header);
        final CircleImageView circleImageView = (CircleImageView) header.findViewById(R.id.civ_image_header);

        mReference.child("profil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profil profil = dataSnapshot.getValue(Profil.class);

                nameTextView.setText(profil.getName());
                mailTextView.setText(profil.getMail());
                Glide.with(getApplicationContext()).load(profil.getPhotoUrl()).skipMemoryCache(true).into(circleImageView);
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
                fragment = ProfilFragment.newInstance("profil", "Profil");
                break;

            case R.id.nav_formation :
                fragment = DataFragment.newInstance("formation", "Formation", "Option : ", "Technologies étudiées : ");
                break;

            case R.id.nav_studie_project:
                fragment = DataFragment.newInstance("projet_etude", "Projet", "Contexte : ", "Description : ");
                break;

            case R.id.nav_vounteer_experience:
                fragment = DataFragment.newInstance("benevolat", "Expérience bénévole", "Rôle : ", "Description : ");
                break;

            case R.id.nav_professional_experience:
                fragment = DataFragment.newInstance("professionnelle", "Expérience professionelle", "Poste : ", "Description : ");
                break;

            case R.id.nav_skill:
                fragment = SkillFragment.newInstance("competence", "Compétences");
                break;

            case R.id.nav_hobbie:
                fragment = DataFragment.newInstance("hobbie", "Centres d'intérêt", "", "");
                break;

            case R.id.nav_contact:
                fragment = ContactFragment.newInstance("contact", "Contact", "", "");
                break;
        }

        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }


        return true;
    }

}
