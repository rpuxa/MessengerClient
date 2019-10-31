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
import kotlinx.android.synthetic.main.item_dialog.view.*
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.getEqualsDiff
import ru.rpuxa.messenger.inflate
import ru.rpuxa.messenger.loadAvatar
import ru.rpuxa.messenger.model.UserLoader

class DialogsAdapter(private val fragment: Fragment) :
    ListAdapter<Int, DialogsAdapter.ViewModel>(getEqualsDiff()) {
    inner class ViewModel(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.dialog_name
        private val time: TextView = view.dialog_time
        private val message: TextView = view.dialog_msg
        private val avatar: ImageView = view.dialog_avatar

        @SuppressLint("SetTextI18n")
        fun bind(item: Int) {
            UserLoader(item).load(fragment.lifecycleScope).user.observe(fragment) {
                name.text = "${it.name} ${it.surname}"
                itemView.context.loadAvatar(it.avatar, avatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewModel(
        parent.inflate(R.layout.item_dialog)
    )

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        holder.bind(getItem(position))
    }
}