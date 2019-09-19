package ru.rpuxa.messenger.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rpuxa.messenger.MutableLiveData
import ru.rpuxa.messenger.model.db.UsersDao
import ru.rpuxa.messenger.model.server.Server
import ru.rpuxa.messenger.view.App
import java.io.IOException
import javax.inject.Inject

class UserLoader(private val id: Int) {

    @Inject
    lateinit var usersDao: UsersDao

    @Inject
    lateinit var server: Server

    init {
        App.component.inject(this)
    }

    fun load(scope: CoroutineScope): LazyUser {
        val status = MutableLiveData(LazyUser.Status.LOADING)
        val user = MutableLiveData<User>()
        val lazyUser = LazyUser(status, user)
        scope.launch(Dispatchers.Main) {
            val fromDataBase = usersDao.load(id)
            if (fromDataBase != null) {
                user.value = fromDataBase
                status.value = LazyUser.Status.LOADED
            }

            try {
                val answer = server.getPublicInfo(id)
                val loadedUser = User(id, answer.login, answer.name, answer.surname, answer.birthday, answer.avatar)
                usersDao.insert(loadedUser)
                user.value = loadedUser
                status.value = LazyUser.Status.LOADED
            } catch (e: IOException) {
                status.value = if (fromDataBase == null) LazyUser.Status.SERVER_ERROR else LazyUser.Status.LOADED_FROM_DATABASE
            }
        }

        return lazyUser
    }
}