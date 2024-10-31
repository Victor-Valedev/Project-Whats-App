package com.victor.projectwhatsapp.Screens

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.adapters.ConversationsAdapter
import com.victor.projectwhatsapp.databinding.ActivityMessageBinding
import com.victor.projectwhatsapp.model.Message
import com.victor.projectwhatsapp.model.User
import com.victor.projectwhatsapp.utils.Constants
import com.victor.projectwhatsapp.utils.showMessage

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val fireStore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var listenerRegistration: ListenerRegistration

    private var recipientData: User? = null
    private lateinit var conversationsAdapter: ConversationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recoverRecipientUserData()
        initializeToolBar()
        initializeClicksEvents()
        initializeRecyclerView()
        initializeListeners()
    }

    private fun initializeRecyclerView() {

        with(binding){
            conversationsAdapter = ConversationsAdapter()
            rvMessage.adapter = conversationsAdapter
            rvMessage.layoutManager = LinearLayoutManager(applicationContext)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration.remove()
    }

    private fun initializeListeners() {

        val idUserSender = firebaseAuth.currentUser?.uid
        val idUserRecipient = recipientData?.id
        if(idUserSender != null && idUserRecipient != null){

            listenerRegistration = fireStore
                .collection(Constants.BD_MESSAGE)
                .document(idUserSender)
                .collection(idUserRecipient)
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot, error ->

                    if(error != null){
                        showMessage("Erro ao recuperar as mensagens")
                    }

                    val documents = querySnapshot?.documents
                    val listMessages = mutableListOf<Message>()

                    documents?.forEach{ documentSnapshot ->

                        val message = documentSnapshot.toObject(Message::class.java)
                        if(message != null){
                            listMessages.add(message)
                            Log.i("exibir_mensg", message.message)
                        }

                    }
                    //lista
                    if(listMessages.isNotEmpty()){
                        //carregar dados adapter
                        conversationsAdapter.addListMessage(listMessages)
                    }

                }

        }


    }

    private fun initializeClicksEvents() {

        binding.fabSendMenssage.setOnClickListener {
            val message = binding.editMessage.text.toString()
            saveMessage(message)
            binding.editMessage.text?.clear()
        }

    }

    private fun saveMessage(textMessage: String) {

        if(textMessage.isNotEmpty()){

            val idUserSender = firebaseAuth.currentUser?.uid
            val idUserRecipient = recipientData?.id
            if(idUserSender != null && idUserRecipient != null){

                val message = Message(
                    idUserSender, textMessage
                )
               //salvar para o remetente
               saveMessageFirestore(
                   idUserSender, idUserRecipient, message
               )

               //salvar a mesma mensagem para o destinatário
                saveMessageFirestore(
                    idUserRecipient, idUserSender, message
                )
            }

        }

    }

    private fun saveMessageFirestore(
        idUserSender: String,
        idUserRecipient: String,
        message: Message
    ) {
        fireStore
            .collection(Constants.BD_MESSAGE)
            .document(idUserSender)
            .collection(idUserRecipient)
            .add(message)
            .addOnFailureListener {
                showMessage("Erro ao enviar mensagem")
            }
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