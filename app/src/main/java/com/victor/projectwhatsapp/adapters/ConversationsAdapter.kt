package com.victor.projectwhatsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.victor.projectwhatsapp.databinding.ItemMessageRecipientBinding
import com.victor.projectwhatsapp.databinding.ItemMessageSenderBinding
import com.victor.projectwhatsapp.model.Message
import com.victor.projectwhatsapp.utils.Constants

class ConversationsAdapter : Adapter<ViewHolder>() {

    private var listMessages = emptyList<Message>()
    fun addListMessage(list: List<Message>){
        listMessages = list
        notifyDataSetChanged()
    }

    class MessageSenderViewHolder(//ViewHolder
        private val binding: ItemMessageSenderBinding
    ) : ViewHolder(binding.root){

        fun bind(message: Message){
            binding.textMessageSender.text = message.message
        }

        companion object{
            fun inflateLayout(parent: ViewGroup) : MessageSenderViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val itemView = ItemMessageSenderBinding.inflate(
                    inflater, parent, false
                )
                return MessageSenderViewHolder(itemView)
            }
        }

    }

    class MessageRecipientViewHolder(//ViewHolder
        private val binding: ItemMessageRecipientBinding
    ) : ViewHolder(binding.root){

        fun bind(message: Message){
            binding.textMessageRecipient.text = message.message
        }

        companion object{
            fun inflateLayout(parent: ViewGroup) : MessageRecipientViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val itemView = ItemMessageRecipientBinding.inflate(
                    inflater, parent, false
                )
                return MessageRecipientViewHolder(itemView)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        val message = listMessages[position]
        val idUserLog = FirebaseAuth.getInstance().currentUser?.uid.toString()

        return if(idUserLog == message.idUser){
            Constants.TYPE_SENDER
        } else {
            Constants.TYPE_RECIPIENT
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(viewType == Constants.TYPE_SENDER){
          return  MessageSenderViewHolder.inflateLayout(parent)
        }

        return  MessageRecipientViewHolder.inflateLayout(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = listMessages[position]
        when(holder){ // verifica o type
            is MessageSenderViewHolder -> holder.bind(message)
            is MessageRecipientViewHolder -> holder.bind(message)
        }

    }

    override fun getItemCount(): Int {
        return listMessages.size
    }

}