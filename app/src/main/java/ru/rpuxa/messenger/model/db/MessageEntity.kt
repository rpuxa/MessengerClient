package ru.rpuxa.messenger.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
class MessageEntity(
    randomUUID: String,
    text: String,
    dialogId: Int,
    val serverId: Int,
    val sender: Int
) : NotSentMessage(randomUUID, text, dialogId)

@Entity(tableName = "not_sent_messages")
open class NotSentMessage(
    @PrimaryKey
    val randomUUID: String,
    val text: String,
    val dialogId: Int
)

