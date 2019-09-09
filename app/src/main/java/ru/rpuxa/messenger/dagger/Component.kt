package ru.rpuxa.messenger.dagger

import dagger.Component
import ru.rpuxa.messenger.dagger.providers.ContextProvider
import ru.rpuxa.messenger.dagger.providers.ServerProvider
import ru.rpuxa.messenger.dagger.providers.ViewModelProvider
import ru.rpuxa.messenger.viewmodel.ViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextProvider::class,
        ServerProvider::class,
        ViewModelProvider::class
    ]
)
interface Component {
    fun inject(viewModelFactory: ViewModelFactory)
}