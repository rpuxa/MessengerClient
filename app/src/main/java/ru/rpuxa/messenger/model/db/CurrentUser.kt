package ru.rpuxa.messenger.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_user")
class CurrentUser(
    var token: String = "",
    var id: Int = 0
) {

    @Deprecated("Field only for database", level = DeprecationLevel.ERROR)
    @PrimaryKey
    var i = 0
}