package com.victor.projectwhatsapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.victor.projectwhatsapp.Screens.ProfileUserActivity
import com.victor.projectwhatsapp.Screens.ScreenLogin
import com.victor.projectwhatsapp.adapters.ViewPagerAdapter
import com.victor.projectwhatsapp.databinding.ActivityMainBinding

class MainActivity : BaseNetworkActivity() {

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

        initializeNavigation()

    }



    private fun initializeNavigation() {

        val tabLayout = binding.tabLayoutPrincipal
        val viewPager = binding.viewPagerPrincipal

        //Adapter
        val tabs = listOf("Conversas", "Status", "Contatos")

        viewPager.adapter = ViewPagerAdapter(
            tabs,supportFragmentManager, lifecycle
        )

        tabLayout.isTabIndicatorFullWidth = true

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = tabs[position]
        }.attach()

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
        val dialogPersonalization = AlertDialog.Builder(this)
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

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(nightModeFlags){
            Configuration.UI_MODE_NIGHT_YES -> {
                dialogPersonalization.setOnShowListener {
                    dialogPersonalization.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.white))
                    dialogPersonalization.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                dialogPersonalization.setOnShowListener {
                    dialogPersonalization.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.black))
                    dialogPersonalization.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.black))
                }
            }
        }

        dialogPersonalization.show()
    }


}