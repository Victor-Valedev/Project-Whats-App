package com.victor.projectwhatsapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.google.firebase.auth.FirebaseAuth
import com.victor.projectwhatsapp.Screens.ProfileUserActivity
import com.victor.projectwhatsapp.Screens.ScreenLogin
import com.victor.projectwhatsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeToolBar()

    }


    private fun initializeToolBar() {
        val toolBar = binding.toolBarMain.tbPrincipal
        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            title = "WhatsApp"
        }

        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_principal, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.item_perfil -> {
                            startActivity(
                                Intent(applicationContext, ProfileUserActivity::class.java)
                            )
                        }

                        R.id.item_opcoes -> {

                        }

                        R.id.item_sair -> {
                            signOut()
                        }
                    }
                    return true
                }

            }
        )

    }

    private fun signOut() {
        AlertDialog.Builder(this)
            .setTitle("Deslogar")
            .setMessage("Deseja realmente sair?")
            .setNegativeButton("Não") { dialog, position -> }
            .setPositiveButton("Sim") { dialog, position ->
                firebaseAuth.signOut()
                startActivity(
                    Intent(applicationContext, ScreenLogin::class.java)
                )
                finish()
            }
            .create()
            .show()

    }


}