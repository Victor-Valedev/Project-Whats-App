package com.victor.projectwhatsapp.Screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.databinding.ActivityScreenRegisterBinding

class ScreenRegister : AppCompatActivity() {

    private val binding by lazy {
        ActivityScreenRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Toolbar
        initializeToolBar()

    }

    private fun initializeToolBar() {
        val toolBar = binding.includeToolBar.tbPrincipal
        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            title = "Faça o seu cadastro"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}