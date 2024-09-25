package com.victor.projectwhatsapp.Screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.databinding.ActivityProfileUserBinding

class ProfileUserActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityProfileUserBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}