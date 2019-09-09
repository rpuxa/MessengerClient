package ru.rpuxa.messenger.dagger.providers

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.rpuxa.messenger.dagger.ViewModelKey
import ru.rpuxa.messenger.viewmodel.LoginViewModel

@Module
abstract class ViewModelProvider {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun player(v: LoginViewModel): ViewModel
}