package ru.rpuxa.messenger.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.rpuxa.messenger.model.User

@Database(
    entities = [
        CurrentUser::class,
        User::class
    ],
    version = 1
)
abstract class MyDataBase : RoomDatabase() {

    abstract val currentUserDao: CurrentUserDao

    abstract val usersDao: UsersDao
}