package fr.project.mlignereux.cv.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Profil(val name: String? = null,
                  val photoUrl: String? = null,
                  val job: String? = null,
                  val where: String? = null,
                  val from: String? = null,
                  val backgroundUrl: String? = null,
                  val description: String? = null,
                  val bornDate: String? = null,
                  val address: String? = null,
                  val state: String? = null,
                  val driversLicense: String? = null,
                  val mail: String? = null)
