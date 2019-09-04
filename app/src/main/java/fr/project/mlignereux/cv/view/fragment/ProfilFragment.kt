package fr.project.mlignereux.cv.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import fr.project.mlignereux.R
import fr.project.mlignereux.cv.model.Profil

class ProfilFragment : Fragment() {
    private val reference: String?
        get() = arguments?.getString("REFERENCE")

    private val title: String?
        get() = arguments?.getString("TITLE")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_profil, container, false)

        val circleImageView = v.findViewById<View>(R.id.civ_image_profil) as CircleImageView
        val imageViewTop = v.findViewById<View>(R.id.image_view_top) as ImageView
        val profilNameTextView = v.findViewById<View>(R.id.profil_name) as TextView
        val profilJobTextView = v.findViewById<View>(R.id.profil_job) as TextView
        val descriptionTextView = v.findViewById<View>(R.id.tv_description_profil) as TextView
        val contactFab = v.findViewById<View>(R.id.fab_contact) as FloatingActionButton

        val mDatabase = FirebaseDatabase.getInstance().reference

        (activity as AppCompatActivity).supportActionBar?.title = title

        reference?.let {
            mDatabase.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val profil = dataSnapshot.getValue(Profil::class.java)

                    Glide.with(requireContext()).load(profil!!.photoUrl).skipMemoryCache(true).into(circleImageView)
                    Glide.with(requireContext()).load(profil.backgroundUrl).skipMemoryCache(true).centerCrop().into(imageViewTop)
                    profilJobTextView.text = profil.job
                    profilNameTextView.text = profil.name
                    descriptionTextView.text = profil.description

                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

        contactFab.setOnClickListener {
            fragmentManager?.beginTransaction()
                    ?.replace(R.id.content_main, ContactFragment.newInstance(getString(R.string.contact_bundle), getString(R.string.contact_title)))
                    ?.commit()
        }

        return v
    }

    companion object {

        private val TAG = "ProfilFragment"

        fun newInstance(reference: String, title: String): Fragment {

            val fragment = ProfilFragment()
            val args = Bundle()
            args.putString("TITLE", title)
            args.putString("REFERENCE", reference)
            fragment.arguments = args

            return fragment
        }
    }
}
