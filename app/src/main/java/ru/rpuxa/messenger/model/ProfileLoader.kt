package ru.rpuxa.messenger.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rpuxa.messenger.MutableLiveData
import ru.rpuxa.messenger.model.db.CurrentUser
import ru.rpuxa.messenger.model.db.CurrentUserDao
import ru.rpuxa.messenger.model.db.UsersDao
import ru.rpuxa.messenger.model.server.Error
import ru.rpuxa.messenger.model.server.Server
import ru.rpuxa.messenger.view.App
import java.io.IOException
import javax.inject.Inject

class ProfileLoader(private val token: String) {

    private val status = MutableLiveData<LazyUser.Status>()
    private val user = MutableLiveData<User>()

    val lazyUser = LazyUser(status, user)

    @Inject
    lateinit var currentUserDao: CurrentUserDao

    @Inject
    lateinit var usersDao: UsersDao

    @Inject
    lateinit var server: Server

    init {
        App.component.inject(this)
    }


    fun load(scope: CoroutineScope) {
        scope.launch(Dispatchers.Main) {
            status.value = LazyUser.Status.LOADING
            val currentUser = currentUserDao.get()
            val id = currentUser.id
            if (id != CurrentUser.ID_NOT_INITIALIZED) {
                val fromDataBase = usersDao.load(id)
                if (fromDataBase != null) {
                    user.value = fromDataBase
                    status.value = LazyUser.Status.LOADED
                }
            }

            try {
                val answer = server.getPrivateInfo(token)
                if (answer.error == Error.UNKNOWN_TOKEN.code) {
                    status.value = LazyUser.Status.TOKEN_DEPRECATED
                    return@launch
                }
                val loadedUser =
                    User(answer.id, answer.login, answer.name, answer.surname, answer.birthday)
                currentUser.id = answer.id
                usersDao.insert(loadedUser)
                currentUserDao.update(currentUser)
                user.value = loadedUser
                status.value = LazyUser.Status.LOADED
            } catch (e: IOException) {
                status.value =
                    if (id == CurrentUser.ID_NOT_INITIALIZED) LazyUser.Status.SERVER_ERROR else LazyUser.Status.LOADED_FROM_DATABASE
            }
        }
    }
}