package com.victor.projectwhatsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.Screens.MessageActivity
import com.victor.projectwhatsapp.adapters.ContactsAdapter
import com.victor.projectwhatsapp.databinding.FragmentContactsBinding
import com.victor.projectwhatsapp.model.User
import com.victor.projectwhatsapp.utils.Constants


class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var eventRegistration: ListenerRegistration
    private lateinit var contactsAdapter: ContactsAdapter

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

        binding = FragmentContactsBinding.inflate(
            inflater, container, false
        )

        contactsAdapter = ContactsAdapter { user ->
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("recipientData", user)
            //intent.putExtra("origin", Constants.ORIGIN_CONTACT)
            startActivity(intent)
        }
        binding.rvContatos.adapter = contactsAdapter

        binding.rvContatos.layoutManager = LinearLayoutManager(context)


        return binding.root

        // Inflate the layout for this fragment
        /*return inflater.inflate(
            R.layout.fragment_contacts,
            container,
            false)*/
    }

    override fun onStart() {
        super.onStart()
        listContactsListener()
    }

    private fun listContactsListener() {

        eventRegistration = firestore
            .collection(Constants.USERS)
            .addSnapshotListener { querySnapShot, error ->

                val listUsers = mutableListOf<User>()
                val documents = querySnapShot?.documents

                documents?.forEach { documentSnapShot ->

                    val user = documentSnapShot.toObject(User::class.java)
                    val idUserLogged = firebaseAuth.currentUser?.uid

                    if (user != null && idUserLogged != null) {
                        if(idUserLogged != user.id){
                            listUsers.add(user)
                        }
                    }
                }
                //Lista de contatos (atualizar recyclerView)
                if(listUsers.isNotEmpty()){
                    contactsAdapter.addList(listUsers)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventRegistration.remove()
    }

}