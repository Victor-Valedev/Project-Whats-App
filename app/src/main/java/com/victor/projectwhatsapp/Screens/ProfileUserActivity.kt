package com.victor.projectwhatsapp.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.databinding.ActivityProfileUserBinding

class ProfileUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileUserBinding

    private var isPermissionCamera = false
    private var isPermissionGallery = false

    // Registrar o resultado da solicitação de permissões fora de métodos de instância
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isPermissionCamera = permissions[Manifest.permission.CAMERA] ?: isPermissionCamera
        isPermissionGallery = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: isPermissionGallery
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeToolBarProfile()

        binding.fbAddPhotoGallery.setOnClickListener {
            requestPermissionsApp()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissionsApp() {
        isPermissionCamera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        isPermissionGallery = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

        // Lista de permissões negadas
        val listPermissionsDenied = mutableListOf<String>()

        if (!isPermissionCamera) {
            listPermissionsDenied.add(Manifest.permission.CAMERA)
        }
        if (!isPermissionGallery) {
            listPermissionsDenied.add(Manifest.permission.READ_MEDIA_IMAGES)
        }

        // Solicitar múltiplas permissões
        if (listPermissionsDenied.isNotEmpty()) {
            permissionsLauncher.launch(listPermissionsDenied.toTypedArray())
        }
    }

    private fun initializeToolBarProfile() {
        val toolBar = binding.includeToolBarProfile.tbPrincipal
        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            title = "Configurações de Perfil"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}