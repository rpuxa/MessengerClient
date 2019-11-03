package ru.rpuxa.messenger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.rpuxa.messenger.model.db.CurrentUserDao
import ru.rpuxa.messenger.model.server.Server
import javax.inject.Inject

class DialogsViewModel @Inject constructor(
    private val currentUserDao: CurrentUserDao,
    private val server: Server
) : ViewModel() {

    private val _dialogs = MutableLiveData(emptyList<Int>())
    val dialogs: LiveData<List<Int>> get() = _dialogs

    private val currentUser = runBlocking { currentUserDao.get() }

    fun load() {
        viewModelScope.launch {
            try {
                _dialogs.value = server.getFriends(currentUser.token).ids
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}