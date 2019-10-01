package fr.project.mlignereux.cv.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Data(val imageUrl: String? = "",
                val title: String? = "",
                val subtitle: String? = "",
                val date: String? = "",
                val description: String? = "")
