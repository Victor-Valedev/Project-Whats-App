package com.victor.projectwhatsapp.Screens

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import com.victor.projectwhatsapp.adapters.MessageAdapter
import com.victor.projectwhatsapp.databinding.ActivityMessageBinding
import com.victor.projectwhatsapp.model.Conversation
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
    private var senderData: User? = null

    private lateinit var conversationsAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recoverDataUsers()
        initializeToolBar()
        initializeClicksEvents()
        initializeRecyclerView()
        initializeListeners()
    }

    private fun initializeRecyclerView() {

        with(binding){
            conversationsAdapter = MessageAdapter()
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
               //Victor -> foto e nome destinatário (vale)
               val convesationSender = Conversation(
                   idUserSender, idUserRecipient,
                   recipientData!!.foto, recipientData!!.nome,
                   textMessage
               )

               saveConversationFirestore(convesationSender)

               //salvar a mesma mensagem para o destinatário
                saveMessageFirestore(
                    idUserRecipient, idUserSender, message
                )
                //Vale -> foto e nome destinatário (victor)
                val conversationRecipient = Conversation(
                    idUserRecipient, idUserSender,
                    senderData!!.foto, senderData!!.nome,
                    textMessage
                )

                saveConversationFirestore(conversationRecipient)
            }

        }
        binding.editMessage.setText("")
    }

    private fun saveConversationFirestore(convesation: Conversation) {

        fireStore
            .collection(Constants.CONVERSATION)
            .document(convesation.idUserSender)
            .collection(Constants.LAST_MESSAGES)
            .document(convesation.idUserRecipient)
            .set(convesation)
            .addOnFailureListener {
                showMessage("Erro ao salvar conversa")
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

    private fun recoverDataUsers() {

        //Dados do usuário logado
        val idUserRecipient = firebaseAuth.currentUser?.uid
        if(idUserRecipient != null){
            fireStore
                .collection(Constants.USERS)
                .document(idUserRecipient)
                .get()
                .addOnSuccessListener {  documentSnapShot ->

                    val user = documentSnapShot.toObject(User::class.java)
                    if(user != null){
                        senderData = user
                    }

                }
        }


        //Recuperando dados destinatário
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