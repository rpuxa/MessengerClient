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

    private val _status = MutableLiveData(Status.NO_ERROR)

    fun login(login: String, password: String) {


        viewModelScope.launch(Dispatchers.Main) {
            _status.value = Status.SIGNING
            _status.value = try {
                val answer = server.login(login, password)

                if (answer.error == 101) {
                    Status.WRONG_LOGIN_OR_PASSWORD
                } else {
                    Status.LOGIN_SUCCSESSFULL
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

        WRONG_NAME,
        WRONG_SURNAME,
        WRONG_LOGIN,

        SERVER_UNAVAILABLE,
        LOGIN_ALREADY_EXISTS,
        WRONG_LOGIN_OR_PASSWORD,

        LOGIN_SUCCSESSFULL
    }
}