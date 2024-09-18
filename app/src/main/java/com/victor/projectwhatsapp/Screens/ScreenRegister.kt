package com.victor.projectwhatsapp.Screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.victor.projectwhatsapp.MainActivity
import com.victor.projectwhatsapp.databinding.ActivityScreenRegisterBinding
import com.victor.projectwhatsapp.utils.showMessage

class ScreenRegister : AppCompatActivity() {

    private val binding by lazy {
        ActivityScreenRegisterBinding.inflate(layoutInflater)
    }

    private lateinit var nome: String
    private lateinit var email: String
    private lateinit var senha: String

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Toolbar
        initializeToolBar()

        //Validation Register
        registerValidationFirebase()

    }

    private fun registerValidationFirebase() {
        binding.btnCadastrar.setOnClickListener {
            if (validateFields()) {
                //Created user
                registerUser(nome, email, senha)
            } else {

            }
        }
    }

    private fun registerUser(nome: String, email: String, senha: String) {

        firebaseAuth.createUserWithEmailAndPassword(
            email, senha
        ).addOnCompleteListener { result ->
            if(result.isSuccessful){
                showMessage("Sucesso ao fazer seu cadastro!")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
            }
        }.addOnFailureListener { error ->
            try {
                throw error
            }catch (errorPassowordWeak: FirebaseAuthWeakPasswordException){
                errorPassowordWeak.printStackTrace()
                showMessage("Sua senha está muito fraca, digite outra com letras, números e caracteres especiais!")
            }catch (errorUserAlreadExists: FirebaseAuthUserCollisionException){
                errorUserAlreadExists.printStackTrace()
                showMessage("Este e-mail já foi cadastrado, tente outro!")
            }catch (errorInvalidCredentials: FirebaseAuthInvalidCredentialsException){
                errorInvalidCredentials.printStackTrace()
                showMessage("E-mail inválido, digite um outro e-mail!")
            }
        }
    }

    private fun validateFields(): Boolean {

        nome = binding.editNome.text.toString()
        email = binding.editEmail.text.toString()
        senha = binding.editSenha.text.toString()

        if (nome.isNotEmpty()) {

            binding.textInputNome.error = null

            if (email.isNotEmpty()) {

                binding.textInputEmail.error = null

                if (senha.isNotEmpty()) {
                    binding.textInputSenha.error = null
                    return true
                } else {
                    binding.textInputSenha.error = "Crie a sua senha!"
                    return false
                }
            } else {
                binding.textInputEmail.error = "Preencha o seu email!"
                return false
            }

        } else {
            binding.textInputNome.error = "Preencha o seu nome!"
            return false
        }
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