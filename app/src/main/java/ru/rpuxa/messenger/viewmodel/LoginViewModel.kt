package ru.rpuxa.messenger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.rpuxa.messenger.model.db.CurrentUser
import ru.rpuxa.messenger.model.db.CurrentUserDao
import ru.rpuxa.messenger.model.server.Server
import java.io.IOException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val server: Server,
    private val currentUserDao: CurrentUserDao
) : ViewModel() {

    val status: LiveData<Status> get() = _status
    val error: LiveData<String?> get() = _error

    private val _status = MutableLiveData(Status.NO_ERROR)
    private val _error = MutableLiveData(null as String?)

    fun login(login: String, password: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _status.value = Status.SIGNING
            _status.value = try {
                val answer = server.login(login, password)

                if (answer.errorText != null) {
                    _error.value = answer.errorText
                    Status.WRONG_REG_DATA
                }
                else {
                    saveToken(answer.token)
                    Status.LOGIN_SUCCESSFUL
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Status.SERVER_UNAVAILABLE
            }
        }
    }

    fun register(
        name: String,
        surname: String,
        login: String,
        password: String,
        repeatPassword: String
    ) {
        if (password != repeatPassword) {
            _status.value = Status.PASSWORDS_NOT_EQUAL
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            _status.value = Status.SIGNING
            _status.value = try {
                val answer = server.reg(login, password, name, surname)
                if (answer.errorText != null) {
                    _error.value = answer.errorText
                    Status.WRONG_REG_DATA
                }
                else {
                    saveToken(answer.token)
                    Status.LOGIN_SUCCESSFUL
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Status.SERVER_UNAVAILABLE
            }
        }
    }

    private suspend fun saveToken(token: String) {
        val user = currentUserDao.get()
        user.token = token
        currentUserDao.update(user)
    }

    fun resetStatus() {
        _status.value = Status.NO_ERROR
    }

    fun isUserAuthorized(): Boolean =
        runBlocking {
            currentUserDao.get().token != CurrentUser.TOKEN_NOT_INITIALIZED
        }


    enum class Status {
        SIGNING,
        NO_ERROR,
        WRONG_REG_DATA,
        SERVER_UNAVAILABLE,
        WRONG_LOGIN_OR_PASSWORD,
        LOGIN_ALREADY_EXISTS,
        PASSWORDS_NOT_EQUAL,
        LOGIN_SUCCESSFUL
    }
}