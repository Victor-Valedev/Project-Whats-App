package com.victor.projectwhatsapp.Screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.databinding.ActivityScreenLoginBinding

class ScreenLogin : AppCompatActivity() {

    private val binding by lazy {
        ActivityScreenLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Mudando tema do App
        binding.buttonTema.setOnCheckedChangeListener{ _, isChecked ->

            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
}