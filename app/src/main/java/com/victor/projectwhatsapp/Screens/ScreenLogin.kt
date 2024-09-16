package com.victor.projectwhatsapp.Screens

import android.content.Intent
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

        //Change App theme
        themeconfig()

        //Open the RegisterActivity
        registerOpenActivity()



    }

    private fun registerOpenActivity() {
        binding.textCadastro.setOnClickListener {
            startActivity(
                Intent(this, ScreenRegister::class.java)
            )
        }
    }

    private fun themeconfig() {
        binding.buttonTema.setOnCheckedChangeListener{ _, isChecked ->

            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}