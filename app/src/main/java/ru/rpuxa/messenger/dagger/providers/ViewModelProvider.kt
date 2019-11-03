package ru.rpuxa.messenger.dagger.providers

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.rpuxa.messenger.dagger.ViewModelKey
import ru.rpuxa.messenger.viewmodel.*

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

    @Binds
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    abstract fun friends(v: FriendsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun chat(v: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DialogsViewModel::class)
    abstract fun dialogs(v: DialogsViewModel): ViewModel
}