package ru.rpuxa.messenger.dagger.providers

import dagger.Module
import dagger.Provides
import ru.rpuxa.messenger.BuildConfig
import ru.rpuxa.messenger.model.server.Server

@Module
class ServerProvider {

    @Provides
    fun server() = Server.create(BuildConfig.SERVER_URL)
}