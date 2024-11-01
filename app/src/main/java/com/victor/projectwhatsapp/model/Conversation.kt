package com.victor.projectwhatsapp.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Conversation(
    val idUserSender: String = "",
    val idUserRecipient: String = "",
    val photo: String = "",
    val name: String = "",
    val lastMessage: String = "",
    @ServerTimestamp
    val date: Date? = null
)
