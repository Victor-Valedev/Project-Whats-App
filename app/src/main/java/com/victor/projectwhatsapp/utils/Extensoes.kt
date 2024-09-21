package com.victor.projectwhatsapp.utils

import android.app.Activity
import android.widget.Toast

fun Activity.showMessage(mensagem: String){
    Toast.makeText(
        this,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}