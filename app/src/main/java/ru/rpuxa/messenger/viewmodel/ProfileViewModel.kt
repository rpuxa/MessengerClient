package ru.rpuxa.messenger.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.rpuxa.messenger.model.ActionsWorker
import ru.rpuxa.messenger.model.ProfileLoader
import ru.rpuxa.messenger.model.db.CurrentUser
import ru.rpuxa.messenger.model.db.CurrentUserDao
import ru.rpuxa.messenger.model.server.Server
import java.io.IOException
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val currentUserDao: CurrentUserDao,
    private val context: Context,
    private val server: Server
) : ViewModel() {

    private lateinit var currentUser: CurrentUser
    private lateinit var profileLoader: ProfileLoader

    val lazyProfile = runBlocking {
        currentUser = currentUserDao.get()
        profileLoader = ProfileLoader(currentUser.token)
        retry()
        profileLoader.lazyUser
    }

    fun retry() {
        profileLoader.load(viewModelScope)
    }

    fun loadLastActionId() {
        viewModelScope.launch {
            while (true) {
                try {
                    val id = server.getLastActionId(currentUser.token).id
                    currentUser.lastActionId = id
                    currentUserDao.update(currentUser)
                    ActionsWorker.start(context)
                    break
                } catch (e: IOException) {
                    e.printStackTrace()
                    delay(2000)
                }
            }
        }
    }

    fun logout() {
        ActionsWorker.stop(context)
        currentUser.token = CurrentUser.TOKEN_NOT_INITIALIZED
        currentUser.id = CurrentUser.ID_NOT_INITIALIZED
        runBlocking {
            currentUserDao.update(currentUser)
        }
    }
}