package ru.rpuxa.messenger.model.server.answers

open class PublicInfoAnswer(
    val login: String,
    val name: String,
    val surname: String,
    val birthday: String?
) : Answer()