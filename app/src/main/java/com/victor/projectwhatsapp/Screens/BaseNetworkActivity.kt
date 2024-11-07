package com.victor.projectwhatsapp.Screens

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseNetworkActivity : AppCompatActivity(){


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            if (!isNetworkAvailable()) {
                val intent = Intent(this, NoInternetActivity::class.java)
                intent.putExtra("previous_activity", this::class.java.name)
                startActivity(intent)
                finish() // Fecha a Activity atual para evitar navegação a telas que não podem funcionar offline
           }
        }

        fun isNetworkAvailable(): Boolean {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }



}