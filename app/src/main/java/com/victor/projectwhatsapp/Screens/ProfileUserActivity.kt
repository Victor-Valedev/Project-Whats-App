package com.victor.projectwhatsapp.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.victor.projectwhatsapp.BaseNetworkActivity
import com.victor.projectwhatsapp.databinding.ActivityProfileUserBinding
import com.victor.projectwhatsapp.utils.ViewUtils
import com.victor.projectwhatsapp.utils.showMessage

class ProfileUserActivity : BaseNetworkActivity() {

    private lateinit var binding: ActivityProfileUserBinding

    private var isPermissionCamera = false
    private var isPermissionGallery = false

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val storage by lazy {
        FirebaseStorage.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var progressBar: ProgressBar

    // Registrar o resultado da solicitação de permissões fora de métodos de instância
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isPermissionCamera = permissions[Manifest.permission.CAMERA] ?: isPermissionCamera
        isPermissionGallery =
            permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: isPermissionGallery
    }

    private val managerGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.imageProfile.setImageURI(uri)
            uploadImageStorage(uri)
        } else {
            showMessage("Nenhuma imagem foi selecionada")
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        initializeToolBarProfile()
        initializaEventsClick()
    }

    override fun onStart() {
        super.onStart()
        recoveredDataUserStart()
    }


    private fun recoveredDataUserStart() {

        ViewUtils.progressBarVisible(progressBar)
        val idUser = firebaseAuth.currentUser?.uid
        if (idUser != null) {
            firestore
                .collection("users")
                .document(idUser)
                .get()
                .addOnSuccessListener { documentSnapShot ->

                    val dataUser = documentSnapShot.data
                    if (dataUser != null) {
                        val name = dataUser["nome"] as String
                        val photo = dataUser["foto"] as String

                        ViewUtils.progressBarGone(progressBar)

                        binding.editTextNameProfile.setText(name)

                        if (photo.isNotEmpty()) {
                            Picasso
                                .get()
                                .load(photo)
                                .into(binding.imageProfile)
                        }

                    }

                }
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initializaEventsClick() {
        binding.fbAddPhotoGallery.setOnClickListener {
            requestPermissionsApp()
            initializeEventsOnclick()
        }

        binding.btnAtualizarPerfil.setOnClickListener {

            val nameUser = binding.editTextNameProfile.text.toString()
            if (nameUser.isNotEmpty()) {

                val idUser = firebaseAuth.currentUser?.uid
                if (idUser != null) {
                    val dataUser = mapOf(
                        "nome" to nameUser
                    )
                    updateDataProfile(idUser, dataUser)
                }
                showMessage("Alterações salvas")

            } else {
                showMessage("Preencha um nome para atualizar")
            }

        }

    }

    private fun uploadImageStorage(uri: Uri) {

        //images -> users -> idUser -> profile.jpg
        ViewUtils.progressBarVisible(progressBar)
        val idUser = firebaseAuth.currentUser?.uid
        if (idUser != null) {
            storage
                .getReference("images")
                .child("users")
                .child(idUser)
                .child("profile.jpg")
                .putFile(uri)
                .addOnSuccessListener { task ->
                    ViewUtils.progressBarGone(progressBar)
                    showMessage("Sucesso ao fazer upload da imagem")

                    task.metadata
                        ?.reference
                        ?.downloadUrl
                        ?.addOnSuccessListener { url ->

                            val dataUser = mapOf(
                                "foto" to url.toString()
                            )

                            updateDataProfile(idUser, dataUser)

                        }

                }.addOnFailureListener {
                    showMessage("Erro ao fazer upload da imagem")
                }
        }
    }

    private fun updateDataProfile(idUser: String, dataUser: Map<String, String>) {
        ViewUtils.progressBarVisible(progressBar)
        firestore
            .collection("users")
            .document(idUser)
            .update(dataUser)
            .addOnSuccessListener {
                ViewUtils.progressBarGone(progressBar)
                //showMessage("Sucesso ao atualizar perfil")
            }
            .addOnFailureListener {
                showMessage("Erro ao atualizar perfil")
            }

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initializeEventsOnclick() {
        if (isPermissionGallery) {
            managerGallery.launch("image/*")
        } else {
            showMessage("Permita o acesso a galeria")
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