package fr.project.mlignereux.cv.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import fr.project.mlignereux.R
import fr.project.mlignereux.cv.model.Contact

class ContactFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    private var isFabOpen: Boolean = false
    private lateinit var fab: FloatingActionButton
    private lateinit var fab1: FloatingActionButton
    private lateinit var fab2: FloatingActionButton
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation
    private lateinit var emailTv: TextView
    private lateinit var callTv: TextView

    private lateinit var database: DatabaseReference

    private var callNumber: String? = null

    private val reference: String?
        get() = arguments?.getString("REFERENCE")

    private val title: String?
        get() = arguments?.getString("TITLE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_contact, container, false)

        fab = v.findViewById<View>(R.id.fab) as FloatingActionButton
        fab1 = v.findViewById<View>(R.id.fab1) as FloatingActionButton
        fab2 = v.findViewById<View>(R.id.fab2) as FloatingActionButton
        callTv = v.findViewById<View>(R.id.tv_call) as TextView
        emailTv = v.findViewById<View>(R.id.tv_email) as TextView

        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        (activity as AppCompatActivity).supportActionBar?.title = title

        fab.setOnClickListener { animateFAB() }

        val stateTextView = v.findViewById<View>(R.id.tv_contact_state) as TextView
        val bornDateTextView = v.findViewById<View>(R.id.tv_contact_born_date) as TextView
        val mailTextView = v.findViewById<View>(R.id.tv_contact_mail) as TextView
        val phoneTextView = v.findViewById<View>(R.id.tv_contact_phone) as TextView
        val addressTextView = v.findViewById<View>(R.id.tv_contact_address) as TextView
        val driversLicenseTextView = v.findViewById<View>(R.id.tv_contact_drivers_license) as TextView
        val linkedinTextView = v.findViewById<View>(R.id.tv_contact_linkedin) as TextView
        val viadeoTextView = v.findViewById<View>(R.id.tv_contact_viadeo) as TextView
        val viadeoImageView = v.findViewById<View>(R.id.iv_contact_viadeo) as ImageView
        val linkedinImageView = v.findViewById<View>(R.id.iv_contact_linkedin) as ImageView


        reference?.let {
            database.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val contact = dataSnapshot.getValue(Contact::class.java)

                    stateTextView.text = contact?.state
                    bornDateTextView.text = contact?.bornDate
                    mailTextView.text = contact?.email
                    phoneTextView.text = contact?.phone
                    addressTextView.text = contact?.address
                    driversLicenseTextView.text = contact?.driversLicense
                    linkedinTextView.text = contact?.linkedin
                    viadeoTextView.text = contact?.viadeo

                    Glide.with(requireContext()).load(contact?.linkedinIcon).skipMemoryCache(true).into(linkedinImageView)
                    Glide.with(requireContext()).load(contact?.viadeoIcon).skipMemoryCache(true).into(viadeoImageView)

                    callNumber = contact?.phone

                    call(callNumber)

                    sendMail(contact?.email)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }



        return v
    }

    private fun call(number: String?) {
        fab1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) !== PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    val builder = AlertDialog.Builder(requireContext())

                    builder.setMessage("Vous devez autoriser l'application à utiliser votre appareil pour téléphoner")

                    builder.setPositiveButton("Ok") { dialog, id -> requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMISSION) }
                    builder.setNegativeButton("Annuler") { dialog, id -> dialog.dismiss() }

                    val dialog = builder.create()

                    dialog.show()

                } else {

                    // No explanation needed, we can request the permission.
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMISSION)
                }
            } else {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + number!!)
                startActivity(callIntent)
            }
        }
    }

    private fun sendMail(email: String?) {

        fab2.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.orEmpty()))
            try {
                startActivity(Intent.createChooser(i, "Send mail..."))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotateBackward)
            fab1.startAnimation(fabClose)
            fab2.startAnimation(fabClose)
            callTv.startAnimation(fabClose)
            emailTv.startAnimation(fabClose)
            fab1.isClickable = false
            fab2.isClickable = false
            isFabOpen = false
        } else {
            fab.startAnimation(rotateForward)
            fab1.startAnimation(fabOpen)
            fab2.startAnimation(fabOpen)
            callTv.startAnimation(fabOpen)
            emailTv.startAnimation(fabOpen)
            fab1.isClickable = true
            fab2.isClickable = true
            isFabOpen = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CALL_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:" + callNumber!!)
                    startActivity(callIntent)
                }
            }
        }
    }

    companion object {

        private val TAG = "ContactFragment"

        private val CALL_PERMISSION = 1

        fun newInstance(reference: String, title: String): ContactFragment {

            val fragment = ContactFragment()
            val args = Bundle()
            args.putString("REFERENCE", reference)
            args.putString("TITLE", title)
            fragment.arguments = args

            return fragment
        }
    }


}
