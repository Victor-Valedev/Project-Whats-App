package com.victor.projectwhatsapp.Screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.victor.projectwhatsapp.MainActivity
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.databinding.ActivityScreenLoginBinding
import com.victor.projectwhatsapp.utils.showMessage

class ScreenLogin : AppCompatActivity() {

    private val binding by lazy {
        ActivityScreenLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var senha: String

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Change App theme
        themeConfig()

        //Open the RegisterActivity
        registerOpenActivity()

        //firebaseAuth.signOut()

    }

    override fun onStart() {
        super.onStart()
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    private fun registerOpenActivity() {
        binding.textCadastro.setOnClickListener {
            startActivity(
                Intent(this, ScreenRegister::class.java)
            )
        }
        binding.btnLogar.setOnClickListener {
            if(validateFields()){
                enterUser()
            }
        }
    }

    private fun enterUser() {

        firebaseAuth.signInWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener {
            showMessage("Logado com sucesso!")
            startActivity(
                Intent(this,MainActivity::class.java)
            )
        }.addOnFailureListener { error ->

            try {
               throw error
            }catch (errorUserInvalidate: FirebaseAuthInvalidUserException){
                errorUserInvalidate.printStackTrace()
                showMessage("E-mail não cadastrado!")
            }catch (errorInvalidateCredentials: FirebaseAuthInvalidCredentialsException){
                showMessage("E-mail ou senha estão incorretos!")
            }
        }

    }

    private fun validateFields(): Boolean {

        email = binding.editLoginEmail.text.toString()
        senha = binding.editLoginSenha.text.toString()

        if(email.isNotEmpty()){
            binding.textInputLayoutLoginEmail.error = null
            if(senha.isNotEmpty()){
                binding.textInputLayoutSenha.error = null
                return true
            }else{
                binding.textInputLayoutSenha.error = "Digite a sua senha!"
                return false
            }
        }else{
            binding.textInputLayoutLoginEmail.error = "Preencha o seu e-mail"
            return false
        }

    }

    private fun themeConfig() {
        binding.buttonTema.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}