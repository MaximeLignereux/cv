package fr.project.mlignereux.cv.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import dagger.android.support.DaggerFragment
import de.hdodenhof.circleimageview.CircleImageView
import fr.project.mlignereux.R
import fr.project.mlignereux.cv.model.Profil
import fr.project.mlignereux.cv.view.ImageLoader
import javax.inject.Inject

class ProfilFragment : DaggerFragment() {

    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        val circleImageView = view.findViewById(R.id.civ_image_profil) as CircleImageView
        val imageViewTop = view.findViewById(R.id.image_view_top) as ImageView
        val profilNameTextView = view.findViewById(R.id.profil_name) as TextView
        val profilJobTextView = view.findViewById(R.id.profil_job) as TextView
        val descriptionTextView = view.findViewById(R.id.tv_description_profil) as TextView
        val contactFab = view.findViewById(R.id.fab_contact) as FloatingActionButton
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.profil_title)

        databaseReference.child(getString(R.string.profil_bundle)).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val profil = dataSnapshot.getValue(Profil::class.java) ?: Profil()
                imageLoader.loadImage(profil.photoUrl, circleImageView)
                imageLoader.loadCenterImage(profil.backgroundUrl, imageViewTop)
                profilJobTextView.text = profil.job
                profilNameTextView.text = profil.name
                descriptionTextView.text = profil.description
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        contactFab.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.content_main, ContactFragment())?.commit()
        }

        return view
    }
}
