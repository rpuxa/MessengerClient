package ru.rpuxa.messenger.view.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.inflate
import ru.rpuxa.messenger.model.db.MessageEntity
import ru.rpuxa.messenger.model.db.NotSentMessage
import ru.rpuxa.messenger.viewmodel.ChatViewModel

class ChatAdapter(private val messagesList: ChatViewModel.MessagesList) :
    ListAdapter<NotSentMessage, ChatAdapter.ViewHolder>(Diff) {

    init {
        messagesList.init()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val text: TextView = view.message_text

        fun bind(message: NotSentMessage) {
            if (message is MessageEntity) {
                text.text = "${message.text}. Отправитель: ${message.sender}"
                messagesList.lookingAt(message.serverId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_message))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object Diff : DiffUtil.ItemCallback<NotSentMessage>() {
        override fun areItemsTheSame(oldItem: NotSentMessage, newItem: NotSentMessage): Boolean =
            oldItem.randomUUID == newItem.randomUUID


        override fun areContentsTheSame(oldItem: NotSentMessage, newItem: NotSentMessage): Boolean {
            return oldItem.text == newItem.text
        }
    }
}