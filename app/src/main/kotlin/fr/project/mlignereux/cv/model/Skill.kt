package fr.project.mlignereux.cv.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Skill(val title: String? = "",
                 val value: Float = 0f)
