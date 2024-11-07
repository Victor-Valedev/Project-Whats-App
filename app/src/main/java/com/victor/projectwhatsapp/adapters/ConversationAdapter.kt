package com.victor.projectwhatsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.squareup.picasso.Picasso
import com.victor.projectwhatsapp.databinding.ItemConversationBinding
import com.victor.projectwhatsapp.model.Conversation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConversationAdapter(
    private val onClick: (Conversation) -> Unit
) : Adapter<ConversationAdapter.ConversationViewHolder>(){
    private var listConversation = emptyList<Conversation>()

    fun addList(list: List<Conversation>){
        listConversation = list
        notifyDataSetChanged()
    }

    inner class ConversationViewHolder(
        private val binding: ItemConversationBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(conversation: Conversation) {
            binding.textUserConversation.text = conversation.name
            binding.textLastMessage.text = conversation.lastMessage


            if(conversation.date != null){

                val formattedDate = formatDate(conversation.date!!)
                //Log.d("ConversationDate", "conversa formatada: ${formattedDate}")
                binding.textHourMesssage.text = formattedDate

            }else{
                binding.textHourMesssage.text = ""
            }



            Picasso.get()
                .load(conversation.photo)
                .into(binding.imagePerfilConversation)


            binding.clItemConversation.setOnClickListener {
                onClick(conversation)
            }

        }

        private fun formatDate(date: Date): String{
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val formattedDate = format.format(date)
            //Log.d("ConversationDate", "Data da conversa: ${formattedDate}")
            return formattedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemConversationBinding.inflate(
            inflater, parent, false
        )

        return ConversationViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {

        val conversation = listConversation[position]
        holder.bind(conversation)

    }

    override fun getItemCount(): Int {
        return listConversation.size
    }

}