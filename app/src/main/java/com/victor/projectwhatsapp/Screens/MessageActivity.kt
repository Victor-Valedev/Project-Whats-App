package com.victor.projectwhatsapp.Screens

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.databinding.ActivityMessageBinding
import com.victor.projectwhatsapp.model.User
import com.victor.projectwhatsapp.utils.Constants

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding

    private var recipientData: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recoverRecipientUserData()
        initializeToolBar()
    }

    private fun initializeToolBar() {

        val toolBar = binding.tbMessage
        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            title = ""
            if(recipientData != null){
                binding.textUserConvertations.text = recipientData!!.nome
                Picasso.get()
                    .load(recipientData!!.foto)
                    .into(binding.imageProfileConversations)
            }
            setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun recoverRecipientUserData() {

        val extras = intent.extras
        if(extras != null){

            val origin = extras.getString("origin")
            if(origin == Constants.ORIGIN_CONTACT){

                recipientData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    extras.getParcelable("recipientData", User::class.java)
                }else{
                    extras.getParcelable("recipientData")
                }

            }else if(origin == Constants.ORIGIN_CONVERSATION){
                //recuperar dados da conversa


            }

        }

    }
}