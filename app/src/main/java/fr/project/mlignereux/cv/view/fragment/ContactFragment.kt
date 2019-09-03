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
import fr.project.mlignereux.cv.R
import fr.project.mlignereux.cv.model.Contact

class ContactFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    private var isFabOpen: Boolean = false
    private lateinit var mFab: FloatingActionButton
    private lateinit var mFab1: FloatingActionButton
    private lateinit var mFab2: FloatingActionButton
    private lateinit var fab_open: Animation
    private lateinit var fab_close: Animation
    private lateinit var rotate_forward: Animation
    private lateinit var rotate_backward: Animation
    private lateinit var mEmailTv: TextView
    private lateinit var mCallTv: TextView

    private lateinit var mDatabase: DatabaseReference

    private var callNumber: String? = null

    val reference: String?
        get() = arguments?.getString("REFERENCE")

    val title: String?
        get() = arguments?.getString("TITLE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseDatabase.getInstance().reference

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_contact, container, false)

        mFab = v.findViewById<View>(R.id.fab) as FloatingActionButton
        mFab1 = v.findViewById<View>(R.id.fab1) as FloatingActionButton
        mFab2 = v.findViewById<View>(R.id.fab2) as FloatingActionButton
        mCallTv = v.findViewById<View>(R.id.tv_call) as TextView
        mEmailTv = v.findViewById<View>(R.id.tv_email) as TextView

        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        rotate_forward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotate_backward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        (activity as AppCompatActivity).supportActionBar?.setTitle(title)

        mFab.setOnClickListener { animateFAB() }

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
            mDatabase.child(it).addValueEventListener(object : ValueEventListener {
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
        mFab1.setOnClickListener {
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

        mFab2.setOnClickListener {
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
            mFab.startAnimation(rotate_backward)
            mFab1.startAnimation(fab_close)
            mFab2.startAnimation(fab_close)
            mCallTv.startAnimation(fab_close)
            mEmailTv.startAnimation(fab_close)
            mFab1.isClickable = false
            mFab2.isClickable = false
            isFabOpen = false
        } else {
            mFab.startAnimation(rotate_forward)
            mFab1.startAnimation(fab_open)
            mFab2.startAnimation(fab_open)
            mCallTv.startAnimation(fab_open)
            mEmailTv.startAnimation(fab_open)
            mFab1.isClickable = true
            mFab2.isClickable = true
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
