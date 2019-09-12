package ru.rpuxa.messenger.dagger.providers

import dagger.Module
import dagger.Provides
import ru.rpuxa.messenger.BuildConfig
import ru.rpuxa.messenger.model.server.Server
import javax.inject.Singleton

@Module
class ServerProvider {

    @Singleton
    @Provides
    fun server() = Server.create(BuildConfig.SERVER_URL)
}