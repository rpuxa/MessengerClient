package ru.rpuxa.messenger.dagger.providers

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.rpuxa.messenger.dagger.ViewModelKey
import ru.rpuxa.messenger.viewmodel.LoginViewModel
import ru.rpuxa.messenger.viewmodel.ProfileEditorViewModel
import ru.rpuxa.messenger.viewmodel.ProfileViewModel

@Module
abstract class ViewModelProvider {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun login(v: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun profile(v: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileEditorViewModel::class)
    abstract fun editor(v: ProfileEditorViewModel): ViewModel
}