package fr.project.mlignereux.cv.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.android.support.DaggerFragment
import fr.project.mlignereux.R
import fr.project.mlignereux.base.util.exception.ExceptionLogger
import fr.project.mlignereux.cv.model.PrivacyPolicy
import javax.inject.Inject

class PrivacyPolicyFragment : DaggerFragment() {

    @Inject
    lateinit var database: DatabaseReference
    @Inject
    lateinit var exceptionLogger: ExceptionLogger

    private lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_privacy_policy, container, false)
        webView = rootView.findViewById(R.id.privacy_webview)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.privaty_policy_title)

        loadPrivacyPolicyUrl()
        return rootView
    }

    private fun loadPrivacyPolicyUrl() {
        database.child(getString(R.string.privacy_policy_bundle)).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {
                val privacyPolicy = data.getValue(PrivacyPolicy::class.java) ?: PrivacyPolicy()
                webView.loadUrl(privacyPolicy.url)
            }

            override fun onCancelled(data: DatabaseError) {
                exceptionLogger.logException(data.toException())
            }

        })
    }
}