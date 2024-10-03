package com.victor.projectwhatsapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.victor.projectwhatsapp.databinding.ActivityNoInternetBinding
import com.victor.projectwhatsapp.utils.showMessage

class NoInternetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoInternetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImageTheme()

        binding.btnTryAgain.setOnClickListener {
            if(isNetworkAvaliable()){
                val previousActivity = intent.getStringExtra("previous_activity")
                if(previousActivity != null) {
                    try {
                        val activityClass = Class.forName(previousActivity)
                        val intent = Intent(this, activityClass)
                        startActivity(intent)
                        finish()
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else{
                    finish()
                }
            }else{
                showMessage("Ainda sem conexão")
            }
        }

    }

    private fun setImageTheme() {

        val imageView = binding.imageView3
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(nightModeFlags){
            Configuration.UI_MODE_NIGHT_YES -> {
                imageView.setImageResource(R.drawable.no_internt_night)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                imageView.setImageResource(R.drawable.no_internet_light)
            }
        }

    }

    private fun isNetworkAvaliable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}