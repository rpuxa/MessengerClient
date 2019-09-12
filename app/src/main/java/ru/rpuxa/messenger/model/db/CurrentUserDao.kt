package ru.rpuxa.messenger.model.db

import androidx.room.*

@Dao
abstract class CurrentUserDao {

    @Query("SELECT * FROM current_user WHERE i == 0")
    protected abstract suspend fun getOrNull(): CurrentUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun set(user: CurrentUser)

    @Transaction
    open suspend fun get(): CurrentUser {
        return getOrNull() ?: CurrentUser().apply {
            set(this)
        }
    }
}