package ru.rpuxa.messenger.view.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_friend.view.*
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.getEqualsDiff
import ru.rpuxa.messenger.inflate
import ru.rpuxa.messenger.loadAvatar
import ru.rpuxa.messenger.model.UserLoader

class FriendsAdapter(private val fragment: Fragment) : ListAdapter<Int, FriendsAdapter.ViewHolder>(getEqualsDiff()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.friend_name
        private val login: TextView = view.friend_login
        private val avatar: ImageView = view.friend_avatar

        @SuppressLint("SetTextI18n")
        fun bind(item: Int) {
            UserLoader(item).load(fragment.lifecycleScope).user.observe(fragment) {
                name.text = "${it.name} ${it.surname}"

                login.text = it.login
                itemView.context.loadAvatar(it.avatar, avatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        parent.inflate(R.layout.item_friend)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}