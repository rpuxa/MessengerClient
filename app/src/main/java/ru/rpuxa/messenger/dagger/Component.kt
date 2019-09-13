package ru.rpuxa.messenger.dagger

import dagger.Component
import ru.rpuxa.messenger.dagger.providers.ContextProvider
import ru.rpuxa.messenger.dagger.providers.DataBaseProvider
import ru.rpuxa.messenger.dagger.providers.ServerProvider
import ru.rpuxa.messenger.dagger.providers.ViewModelProvider
import ru.rpuxa.messenger.model.ProfileLoader
import ru.rpuxa.messenger.model.UserLoader
import ru.rpuxa.messenger.viewmodel.ViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextProvider::class,
        ServerProvider::class,
        ViewModelProvider::class,
        DataBaseProvider::class
    ]
)
interface Component {
    fun inject(viewModelFactory: ViewModelFactory)
    fun inject(viewModelFactory: UserLoader)
    fun inject(profileLoader: ProfileLoader)
}