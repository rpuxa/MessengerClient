package ru.rpuxa.messenger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.rpuxa.messenger.model.db.CurrentUserDao
import ru.rpuxa.messenger.model.server.Error
import ru.rpuxa.messenger.model.server.Server
import java.io.IOException
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
    private val server: Server,
    private val currentUserDao: CurrentUserDao
) : ViewModel() {

    val currentUser = runBlocking { currentUserDao.get() }
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus

    private val _requestStatus = MutableLiveData(RequestStatus.NONE)

    fun loadAllFriends() {

    }

    fun sendFriendRequest(login: String) {
        _requestStatus.value = RequestStatus.SENDING
        viewModelScope.launch {
            val token = currentUser.token
            _requestStatus.value = try {
                val answer = server.sendFriendRequest(token, login)

                when (answer.error) {
                    Error.NO_ERROR.code -> RequestStatus.REQUEST_SENT
                    Error.ACCOUNT_IS_NOT_FOUND.code -> RequestStatus.ACCOUNT_IS_NOT_FOUND
                    Error.ALREADY_IN_FRIENDS.code -> RequestStatus.ALREADY_IN_FRIENDS
                    Error.ALREADY_SENT_REQUEST.code -> RequestStatus.ALREADY_SENT_REQUEST
                    Error.CANT_SEND_REQUEST_TO_YOURSELF.code -> RequestStatus.CANT_SEND_REQUEST_TO_YOURSELF
                    else -> error("Unknown error")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                RequestStatus.SERVER_ERROR
            }
        }
    }

    enum class RequestStatus {
        NONE,
        SENDING,
        REQUEST_SENT,
        ACCOUNT_IS_NOT_FOUND,
        ALREADY_IN_FRIENDS,
        ALREADY_SENT_REQUEST,
        CANT_SEND_REQUEST_TO_YOURSELF,
        SERVER_ERROR,
    }
}