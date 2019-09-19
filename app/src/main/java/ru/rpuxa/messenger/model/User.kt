package ru.rpuxa.messenger.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(
    @PrimaryKey
    var id: Int,
    var login: String,
    var name: String,
    var surname: String,
    var birthday: String?,
    var avatar: String?
)

