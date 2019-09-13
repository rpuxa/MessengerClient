package ru.rpuxa.messenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.runBlocking
import ru.rpuxa.messenger.model.ProfileLoader
import ru.rpuxa.messenger.model.db.CurrentUser
import ru.rpuxa.messenger.model.db.CurrentUserDao
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val currentUserDao: CurrentUserDao
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

    fun logout() {
        currentUser.token = CurrentUser.TOKEN_NOT_INITIALIZED
        currentUser.id = CurrentUser.ID_NOT_INITIALIZED
        runBlocking {
            currentUserDao.update(currentUser)
        }
    }
}