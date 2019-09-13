package ru.rpuxa.messenger.model.server

enum class Error(val code: Int) {
    NO_ERROR(0),
    WRONG_ARGS(1),
    UNKNOWN_TOKEN(2),
    UNKNOWN_ID(3),
    UNKNOWN_USER_FIELD(200),
    ;
}