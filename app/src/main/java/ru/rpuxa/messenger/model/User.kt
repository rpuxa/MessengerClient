package ru.rpuxa.messenger.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "users")
class User(
    @PrimaryKey
    val id: Int,
    val login: String,
    val name: String,
    val surname: String,
    val birthday: Long?
) {

    val birthdayString
        get() = if (birthday == null) null else DATE_FORMAT.format(Date(birthday))

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    }
}