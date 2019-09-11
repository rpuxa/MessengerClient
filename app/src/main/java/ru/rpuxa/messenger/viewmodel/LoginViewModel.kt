package ru.rpuxa.messenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rpuxa.messenger.MutableLiveData
import ru.rpuxa.messenger.model.server.Server
import java.io.IOException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val server: Server
) : ViewModel() {

    val status get() = _status
    val error get() = _error

    private val _status = MutableLiveData(Status.NO_ERROR)
    private val _error = MutableLiveData(null as String?)

    fun login(login: String, password: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _status.value = Status.SIGNING
            _status.value = try {
                val answer = server.login(login, password)

                if (answer.error == 101) {
                    Status.WRONG_LOGIN_OR_PASSWORD
                } else {
                    Status.LOGIN_SUCCESSFUL
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Status.SERVER_UNAVAILABLE
            }
        }
    }

    fun register(name: String, surname: String, login: String, password: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _status.value = Status.SIGNING
            _status.value = try {
                val answer = server.reg(login, password, name, surname)

                when(answer.error) {
                    0 -> Status.LOGIN_SUCCESSFUL
                    100 -> Status.LOGIN_ALREADY_EXISTS
                    else -> {
                        _error.value = answer.errorText
                        Status.WRONG_REG_DATA
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Status.SERVER_UNAVAILABLE
            }
        }
    }

    enum class Status {
        SIGNING,
        NO_ERROR,

        WRONG_REG_DATA,

        SERVER_UNAVAILABLE,
        WRONG_LOGIN_OR_PASSWORD,
        LOGIN_ALREADY_EXISTS,

        LOGIN_SUCCESSFUL
    }
}