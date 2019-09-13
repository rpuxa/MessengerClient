package ru.rpuxa.messenger.model

import androidx.lifecycle.LiveData

class LazyUser(
    val status: LiveData<Status>,
    val user: LiveData<User>
) {
    enum class Status {
        LOADING,
        SERVER_ERROR,
        LOADED,
        LOADED_FROM_DATABASE,
        TOKEN_DEPRECATED
    }
}