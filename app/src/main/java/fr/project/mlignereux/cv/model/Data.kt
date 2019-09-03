package fr.project.mlignereux.cv.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Data(val imageUrl: String? = null,
                val title: String? = null,
                val subtitle: String? = null,
                val date: String? = null,
                val description: String? = null)
