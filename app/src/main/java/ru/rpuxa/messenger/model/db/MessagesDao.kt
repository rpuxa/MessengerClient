package ru.rpuxa.messenger.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface MessagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(msg: List<MessageEntity>)

    @Query("SELECT MAX(serverId) FROM messages WHERE dialogId = :dialogId")
    suspend fun lastMessage(dialogId: Int): Int?

    @Query("SELECT * FROM messages WHERE dialogId = :dialogId AND serverId = :serverId")
    suspend fun get(dialogId: Int, serverId: Int): MessageEntity?
}