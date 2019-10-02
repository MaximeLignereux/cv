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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import dagger.android.support.DaggerFragment
import fr.project.mlignereux.R
import fr.project.mlignereux.cv.model.Contact
import fr.project.mlignereux.cv.view.ImageLoader
import javax.inject.Inject

class ContactFragment : DaggerFragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var database: DatabaseReference

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

    private lateinit var callNumber: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        fab = view.findViewById(R.id.fab) as FloatingActionButton
        fab1 = view.findViewById(R.id.fab1) as FloatingActionButton
        fab2 = view.findViewById(R.id.fab2) as FloatingActionButton
        callTv = view.findViewById(R.id.tv_call) as TextView
        emailTv = view.findViewById(R.id.tv_email) as TextView

        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.contact_title)

        fab.setOnClickListener { animateFAB() }

        val stateTextView = view.findViewById(R.id.tv_contact_state) as TextView
        val bornDateTextView = view.findViewById(R.id.tv_contact_born_date) as TextView
        val mailTextView = view.findViewById(R.id.tv_contact_mail) as TextView
        val phoneTextView = view.findViewById(R.id.tv_contact_phone) as TextView
        val addressTextView = view.findViewById(R.id.tv_contact_address) as TextView
        val driversLicenseTextView = view.findViewById(R.id.tv_contact_drivers_license) as TextView
        val linkedinTextView = view.findViewById(R.id.tv_contact_linkedin) as TextView
        val viadeoTextView = view.findViewById(R.id.tv_contact_viadeo) as TextView
        val viadeoImageView = view.findViewById(R.id.iv_contact_viadeo) as ImageView
        val linkedinImageView = view.findViewById(R.id.iv_contact_linkedin) as ImageView

        database.child(getString(R.string.contact_bundle)).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val contact = dataSnapshot.getValue(Contact::class.java) ?: Contact()

                stateTextView.text = contact.state
                bornDateTextView.text = contact.bornDate
                mailTextView.text = contact.email
                phoneTextView.text = contact.phone
                addressTextView.text = contact.address
                driversLicenseTextView.text = contact.driversLicense
                linkedinTextView.text = contact.linkedin
                viadeoTextView.text = contact.viadeo

                imageLoader.loadImage(contact.linkedinIcon, linkedinImageView)
                imageLoader.loadImage(contact.viadeoIcon, viadeoImageView)

                callNumber = contact.phone ?: ""
                call(callNumber)

                sendMail(contact.email)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return view
    }

    private fun call(number: String?) {
        fab1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {

                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Vous devez autoriser l'application à utiliser votre appareil pour téléphoner")
                    builder.setPositiveButton("Ok") { dialog, id ->
                        requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMISSION)
                    }
                    builder.setNegativeButton("Annuler") { dialog, id -> dialog.dismiss() }

                    val dialog = builder.create()
                    dialog.show()

                } else {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMISSION)
                }
            } else {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$number")
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CALL_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:$callNumber")
                    startActivity(callIntent)
                }
            }
        }
    }

    companion object {
        private const val CALL_PERMISSION = 1
    }

}
