package fr.project.mlignereux.cv.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Profil(val name: String? = "",
                  val photoUrl: String? = "",
                  val job: String? = "",
                  val where: String? = "",
                  val from: String? = "",
                  val backgroundUrl: String? = "",
                  val description: String? = "",
                  val bornDate: String? = "",
                  val address: String? = "",
                  val state: String? = "",
                  val driversLicense: String? = "",
                  val mail: String? = "")
