package ru.rpuxa.messenger.view

import android.app.Application
import ru.rpuxa.messenger.dagger.Component
import ru.rpuxa.messenger.dagger.DaggerComponent
import ru.rpuxa.messenger.dagger.providers.ContextProvider
import ru.rpuxa.messenger.dagger.providers.ServerProvider

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        component = DaggerComponent.builder()
            .contextProvider(ContextProvider(this))
            .serverProvider(ServerProvider())
            .build()
    }

    companion object {
        lateinit var component: Component
            private set
    }
}