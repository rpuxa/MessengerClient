package ru.rpuxa.messenger.model.server.answers

open class PrivateInfoAnswer(
    val id: Int,
    login: String,
    name: String,
    surname: String,
    birthday: Long?
) : PublicInfoAnswer(login, name, surname, birthday)