package fr.project.mlignereux.cv.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Contact(val phone: String? = "",
                   val email: String? = "",
                   val address: String? = "",
                   val linkedin: String? = "",
                   val viadeo: String? = "",
                   val bornDate: String? = "",
                   val state: String? = "",
                   val driversLicense: String? = "",
                   val linkedinIcon: String? = "",
                   val viadeoIcon: String? = "")
