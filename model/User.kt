package com.example.vlad.deliveryinstantapp.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var userId: String? = "",
    var username: String? = "",
    var email: String? = "",
    var role: Boolean? = false,
    var ban: Boolean? = false
)
