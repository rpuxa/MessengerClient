package ru.rpuxa.messenger.view.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.startActivity
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.loadAvatar
import ru.rpuxa.messenger.view.adapters.ChatAdapter
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.ChatViewModel


class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModel()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val id = intent.extras!!.getInt(USER_ID)
        viewModel.load(id)

        viewModel.lazyUser.user.observe(this) {
            chat_name.text = "${it.name} ${it.surname}"
            loadAvatar(it.avatar, chat_avatar)
        }

        val adapter = ChatAdapter(viewModel.messages)
        chat_list.adapter = adapter
        chat_list.layoutManager = LinearLayoutManager(this)

        viewModel.messages.liveData.observe(this) {
            adapter.submitList(it)
        }
    }


    companion object {
        private const val USER_ID = "uesrid"

        fun start(context: Context, id: Int) {
            context.startActivity<ChatActivity>(
                USER_ID to id
            )
        }
    }
}

