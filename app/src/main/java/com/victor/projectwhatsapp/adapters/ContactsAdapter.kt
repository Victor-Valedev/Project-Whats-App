package com.victor.projectwhatsapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.victor.projectwhatsapp.R
import com.victor.projectwhatsapp.databinding.ItemContactBinding
import com.victor.projectwhatsapp.model.User

class ContactsAdapter() : Adapter<ContactsAdapter.ContactViewHolder>(){

    private var listContacts = emptyList<User>()


    fun addList(list: List<User>){
        listContacts = list
        notifyDataSetChanged()
    }

    inner class ContactViewHolder(
        private val binding: ItemContactBinding
    ) : ViewHolder(binding.root){

        fun bind(user: User) {
            binding.textContatoNome.text = user.nome
            if (user.foto.isNullOrEmpty()) {
                binding.imageContatoList.setImageResource(R.drawable.perfil)
            } else {
                Picasso.get()
                    .load(user.foto)
                    .into(binding.imageContatoList)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemContactBinding.inflate(
            inflater, parent, false
        )

        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val user = listContacts[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }
}