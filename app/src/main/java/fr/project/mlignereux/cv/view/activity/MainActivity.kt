package fr.project.mlignereux.cv.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import fr.project.mlignereux.cv.R
import fr.project.mlignereux.cv.model.Profil
import fr.project.mlignereux.cv.view.fragment.ContactFragment
import fr.project.mlignereux.cv.view.fragment.DataFragment
import fr.project.mlignereux.cv.view.fragment.ProfilFragment
import fr.project.mlignereux.cv.view.fragment.SkillFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var mReference: DatabaseReference
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, ProfilFragment.newInstance("profil", "Profil"))
                .commit()

        val header = navigationView.getHeaderView(0)

        val nameTextView = header.findViewById(R.id.tv_text_header) as TextView
        val mailTextView = header.findViewById(R.id.tv_mail_header) as TextView
        val circleImageView = header.findViewById(R.id.civ_image_header) as CircleImageView

        mReference = FirebaseDatabase.getInstance().reference
        mReference.keepSynced(true)
        mReference.child(getString(R.string.profil_bundle)).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profil = dataSnapshot.getValue(Profil::class.java)

                nameTextView.text = profil?.name
                mailTextView.text = profil?.mail
                Glide.with(applicationContext).load(profil?.photoUrl).skipMemoryCache(true).into(circleImageView)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        var fragment: Fragment? = null

        when (id) {
            R.id.nav_profil -> {
                fragment = ProfilFragment.newInstance(getString(R.string.profil_bundle), getString(R.string.profil_title))
                return true
            }
            R.id.nav_formation -> {
                fragment = DataFragment.newInstance(getString(R.string.formation_bundle), getString(R.string.formation_title), getString(R.string.option_data), getString(R.string.techno_data))
            }
            R.id.nav_studie_project -> {
                fragment = DataFragment.newInstance(getString(R.string.project_bundle), getString(R.string.project_title), getString(R.string.contexte_data), getString(R.string.description_data))
            }
            R.id.nav_vounteer_experience ->                     {
                fragment = DataFragment.newInstance(getString(R.string.volunteer_bundle), getString(R.string.volunteer_title), getString(R.string.role_data), getString(R.string.description_data))
            }
            R.id.nav_professional_experience -> {
                fragment = DataFragment.newInstance(getString(R.string.professional_bundle), getString(R.string.professional_title), getString(R.string.poste_data), getString(R.string.description_data))
            }
            R.id.nav_skill -> {
                fragment = SkillFragment.newInstance(getString(R.string.skill_bundle), getString(R.string.skill_title))
            }
            R.id.nav_hobbie -> {
                fragment = DataFragment.newInstance(getString(R.string.hobbies_bundle), getString(R.string.hobbies_title), "", "")
            }
            R.id.nav_contact -> {
                fragment = ContactFragment.newInstance(getString(R.string.contact_bundle), getString(R.string.contact_title))
            }
        }

        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit()
            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
        }
        return true
    }

    companion object {
        private val TAG = "MainActivity"
    }

}
