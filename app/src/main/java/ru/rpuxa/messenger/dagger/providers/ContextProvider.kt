package ru.rpuxa.messenger.dagger.providers

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.rpuxa.messenger.view.App


@Module
class ContextProvider(private val app: App) {

    @Provides
    fun context(): Context = app
}