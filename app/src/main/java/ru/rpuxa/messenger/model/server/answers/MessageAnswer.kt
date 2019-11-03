package ru.rpuxa.messenger.model.server.answers

import ru.rpuxa.messenger.model.db.MessageEntity

class MessageAnswer(
    val id: Int,
    val randomUUID: String,
    val text: String,
    val sender: Int
) : Answer() {


    fun toMessageEntity(dialogId: Int): MessageEntity = MessageEntity(
        randomUUID,
        text,
        dialogId,
        id,
        sender
    )
}