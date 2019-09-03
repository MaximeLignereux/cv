package fr.project.mlignereux.cv.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Contact(val phone: String? = null,
                   val email: String? = null,
                   val address: String? = null,
                   val linkedin: String? = null,
                   val viadeo: String? = null,
                   val bornDate: String? = null,
                   val state: String? = null,
                   val driversLicense: String? = null,
                   val linkedinIcon: String? = null,
                   val viadeoIcon: String? = null)
