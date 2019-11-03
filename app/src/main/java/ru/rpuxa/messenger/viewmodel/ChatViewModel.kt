package ru.rpuxa.messenger.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import ru.rpuxa.messenger.RxBus
import ru.rpuxa.messenger.model.LazyUser
import ru.rpuxa.messenger.model.UserLoader
import ru.rpuxa.messenger.model.db.CurrentUserDao
import ru.rpuxa.messenger.model.db.MessageEntity
import ru.rpuxa.messenger.model.db.MessagesDao
import ru.rpuxa.messenger.model.server.Server
import ru.rpuxa.messenger.view.App
import rx.Subscription
import javax.inject.Inject
import kotlin.math.abs


class ChatViewModel @Inject constructor(
    private val server: Server,
    private val messagesDao: MessagesDao,
    currentUserDao: CurrentUserDao
) : ViewModel() {

    lateinit var lazyUser: LazyUser
        private set

    private var dialogId: Int = -1
    private val token = runBlocking { currentUserDao.get().token }
    val messages = MessagesList()

    private var subscription: Subscription? = null

    fun load(id: Int) {
        lazyUser = UserLoader(id).load(viewModelScope)
        dialogId = id
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.unsubscribe()
    }


    inner class MessagesList {

        val liveData: LiveData<List<MessageEntity>> get() = _liveData


        private val _liveData = MutableLiveData(emptyList<MessageEntity>())
        @SuppressLint("UseSparseArrays")
        private val loaded = HashMap<Int, MessageEntity>()
        private val toLoad = HashSet<Int>()
        private var lastMessage = Int.MAX_VALUE / 2
        @Volatile
        private var newMessage = false

        private val dispatcher = newFixedThreadPoolContext(COROUTINES_COUNT, "messages dispatcher")

        @Inject
        lateinit var bus: RxBus

        init {
            App.component.inject(this)

            subscription = bus.bus.subscribe {
                newMessage = true
                load(lastMessage, 10)
            }
        }

        private fun startCoroutine() {
            viewModelScope.launch(dispatcher) {
                while (true) {
                    val (start, end) =
                        synchronized(toLoad) {
                            if (newMessage) {
                                newMessage = false
                                lastMessage = Int.MAX_VALUE / 2
                            }
                            val iterator = toLoad.iterator()
                            var update = false
                            while (iterator.hasNext()) {
                                val serverId = iterator.next()
                                if (serverId !in 1..lastMessage) {
                                    iterator.remove()
                                    continue
                                }
                                val msg = runBlocking { messagesDao.get(dialogId, serverId) }
                                    ?: continue
                                loaded[msg.serverId] = msg
                                iterator.remove()
                                update = true
                            }

                            if (toLoad.isEmpty()) {
                                if (update) update()
                                return@launch
                            }

                            val sorted = toLoad.sorted()
                            var first = sorted.first()
                            var last = sorted.first()
                            var bestLength = -1
                            var best: Pair<Int, Int>? = null
                            sorted.forEach {
                                if (abs(last - it) > 1) {
                                    first = it
                                }
                                last = it

                                val length = it - first
                                if (length > bestLength) {
                                    bestLength = length
                                    best = first to it
                                }
                            }

                            best!!.apply {
                                var i = first
                                while (i <= second) {
                                    toLoad.remove(i)
                                    i++
                                }
                            }
                        }
                    val messages1 = server.getMessages(token, dialogId, start, end - start)
                    val fromServer = messages1
                        .map { it.toMessageEntity(dialogId) }
                    val maxBy = fromServer.maxBy { it.serverId }?.serverId ?: start - 1
                    if (maxBy < end) {
                        lastMessage = maxBy
                    }
                    messagesDao.insert(fromServer)
                    fromServer.forEach {
                        loaded[it.serverId] = it
                    }

                    update()
                }
            }
        }

        private fun update() {
            val toList = loaded.toSortedMap().values.toList()
            _liveData.postValue(toList)
        }

        private fun load(start: Int, limit: Int) {
            synchronized(toLoad) {
                toLoad.addAll(start until start + limit)
            }
            startCoroutine()
        }

        fun init() {
            viewModelScope.launch {
                val start = messagesDao.lastMessage(dialogId) ?: 0
                load(start, INIT_SIZE)
            }
        }

        fun lookingAt(id: Int) {
            load(id - PAGE_SIZE, 2 * PAGE_SIZE)
        }
    }

    companion object {
        const val INIT_SIZE = 20
        const val PAGE_SIZE = 10
        const val COROUTINES_COUNT = 3
    }
}

