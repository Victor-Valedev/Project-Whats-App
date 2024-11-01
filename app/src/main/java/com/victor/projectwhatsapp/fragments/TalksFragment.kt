package com.victor.projectwhatsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.databinding.FragmentTalksBinding
import com.victor.projectwhatsapp.model.Conversation
import com.victor.projectwhatsapp.utils.Constants
import com.victor.projectwhatsapp.utils.showMessage


class TalksFragment : Fragment() {

    private lateinit var binding: FragmentTalksBinding
    private lateinit var eventSnapshot: ListenerRegistration

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       binding = FragmentTalksBinding.inflate(
            inflater, container, false
        )
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        addListenerConversation()
    }

    private fun addListenerConversation() {

        val idUserSender = firebaseAuth.currentUser?.uid
        if(idUserSender != null){
            eventSnapshot = firestore
                .collection(Constants.CONVERSATION)
                .document(idUserSender)
                .collection(Constants.LAST_MESSAGES)
                .addSnapshotListener { querySnapShot, error ->
                    if(error != null){
                        activity?.showMessage("Erro ao recuperar conversas")
                    }

                    val listConversations = mutableListOf<Conversation>()
                    val documents = querySnapShot?.documents

                    documents?.forEach{ documentSnapshot ->

                        val conversation = documentSnapshot.toObject(Conversation::class.java)
                        if(conversation != null){
                            listConversations.add(conversation)
                            Log.i("exibirmsg", "${conversation.name} - ${conversation.lastMessage}")
                        }

                    }

                    //Atualizar adapter
                    if(listConversations.isNotEmpty()){

                    }

                }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        eventSnapshot.remove()
    }

}