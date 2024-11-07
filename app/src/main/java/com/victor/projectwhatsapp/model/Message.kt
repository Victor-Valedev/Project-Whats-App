package com.victor.projectwhatsapp.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val idUser: String = "",
    val message: String = "",
    @ServerTimestamp
    val date: Date? = null
)
