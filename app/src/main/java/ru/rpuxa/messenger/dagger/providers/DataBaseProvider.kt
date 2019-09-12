package ru.rpuxa.messenger.dagger.providers

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.rpuxa.messenger.model.db.MyDataBase
import javax.inject.Singleton

@Module
class DataBaseProvider {

    @Singleton
    @Provides
    fun db(context: Context) =
        Room.databaseBuilder(context, MyDataBase::class.java, "database.db")
            .build()


    @Provides
    fun currentUser(db: MyDataBase) = db.currentUserDao
}