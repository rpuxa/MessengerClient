package ru.rpuxa.messenger.view.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_friend_request.view.*
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.getEqualsDiff
import ru.rpuxa.messenger.inflate
import ru.rpuxa.messenger.loadAvatar
import ru.rpuxa.messenger.model.UserLoader
import ru.rpuxa.messenger.viewmodel.FriendsViewModel


class FriendRequestAdapter(private val lifecycle: LifecycleOwner, private val viewModel: FriendsViewModel) :
    ListAdapter<Int, FriendRequestAdapter.ViewHolder>(getEqualsDiff()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.friend_request_name
        private val accept: Button = view.friend_request_accept
        private val decline: Button = view.friend_request_decline
        private val avatar: ImageView = view.friend_request_avatar

        @SuppressLint("SetTextI18n")
        fun bind(item: Int) {
            UserLoader(item).load(lifecycle.lifecycle.coroutineScope).user.observe(lifecycle) {
                name.text = "${it.name} ${it.surname}"
                itemView.context.loadAvatar(it.avatar, avatar)
            }

            accept.setOnClickListener {
                viewModel.answerOnRequest(item, true)
            }

            decline.setOnClickListener {
                viewModel.answerOnRequest(item, false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        parent.inflate(
            R.layout.item_friend_request
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}