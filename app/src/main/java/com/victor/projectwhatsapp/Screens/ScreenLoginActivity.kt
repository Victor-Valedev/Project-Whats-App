package com.victor.projectwhatsapp.Screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.victor.projectwhatsapp.databinding.ActivityScreenLoginBinding
import com.victor.projectwhatsapp.utils.ViewUtils
import com.victor.projectwhatsapp.utils.showMessage

class ScreenLoginActivity : BaseNetworkActivity() {

    private val binding by lazy {
        ActivityScreenLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var senha: String

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var buttonSingIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedTheme()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //checkInternet()
        progressBar = binding.progressBar2
        buttonSingIn = binding.btnLogar



        //Change App theme
        themeConfig()

        //Open the RegisterActivity
        registerOpenActivity()

        //firebaseAuth.signOut()

    }


    /*private fun checkInternet() {
        if (!isNetworkAvailable()) {
            val intent = Intent(this, NoInternetActivity::class.java)
            startActivity(intent)
            finish()
        }
    }*/

    private fun applySavedTheme() {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("isDarkMode", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    override fun onStart() {
        super.onStart()
        checkCurrentUser()
    }


    private fun checkCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }
    }

    private fun registerOpenActivity() {
        binding.textCadastro.setOnClickListener {
            startActivity(
                Intent(this, ScreenRegisterActivity::class.java)
            )
        }
        binding.btnLogar.setOnClickListener {
            val originalTextButton = buttonSingIn.text.toString()
            buttonSingIn.text = ""
            ViewUtils.progressBarVisible(progressBar)

            if (validateFields()) {
                enterUser(originalTextButton)
            }else{
                buttonSingIn.text = originalTextButton
                ViewUtils.progressBarGone(progressBar)
            }
        }
    }

    private fun enterUser(originalTextButton: String) {

        firebaseAuth.signInWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener {
            showMessage("Logado com sucesso!")
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }.addOnFailureListener { error ->

            try {
                throw error
            } catch (errorUserInvalidate: FirebaseAuthInvalidUserException) {
                errorUserInvalidate.printStackTrace()
                showMessage("E-mail não cadastrado!")
            } catch (errorInvalidateCredentials: FirebaseAuthInvalidCredentialsException) {
                errorInvalidateCredentials.printStackTrace()
                showMessage("E-mail ou senha estão incorretos!")
            }
        }.addOnCompleteListener {
            ViewUtils.progressBarGone(progressBar)
            buttonSingIn.text = originalTextButton
        }

    }

    private fun validateFields(): Boolean {

        email = binding.editLoginEmail.text.toString()
        senha = binding.editLoginSenha.text.toString()

        if (email.isNotEmpty()) {
            binding.textInputLayoutLoginEmail.error = null
            if (senha.isNotEmpty()) {
                binding.textInputLayoutSenha.error = null
                return true
            } else {
                binding.textInputLayoutSenha.error = "Digite a sua senha!"
                return false
            }
        } else {
            binding.textInputLayoutLoginEmail.error = "Preencha o seu e-mail"
            return false
        }

    }

    private fun isDarkThemeOn(): Boolean {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isDarkMode", false)
    }
    private fun themeConfig() {

        binding.buttonTema.isChecked = isDarkThemeOn()

        binding.buttonTema.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveThemePreference(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveThemePreference(false)
            }
        }
    }

    private fun saveThemePreference(isDarkMode: Boolean) {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isDarkMode", isDarkMode)
            apply()
        }
    }


}