package ru.rpuxa.messenger.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_user")
class CurrentUser(
    var token: String = TOKEN_NOT_INITIALIZED,
    var id: Int = ID_NOT_INITIALIZED,
    var lastActionId: Int = -1
) {

    @Deprecated("Field only for database", level = DeprecationLevel.ERROR)
    @PrimaryKey
    var i = 0
    
    companion object {
        const val TOKEN_NOT_INITIALIZED = ""
        const val ID_NOT_INITIALIZED = 0
    }
}