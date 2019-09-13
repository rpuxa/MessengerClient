package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.findFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.startActivity
import ru.rpuxa.messenger.*
import ru.rpuxa.messenger.model.LazyUser
import ru.rpuxa.messenger.view.LoginActivity
import ru.rpuxa.messenger.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModel()

    private val profileLoading by lazy { childFragmentManager.findFragmentById(R.id.profile_loading) as LoadingFragment }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profile, container, false).apply {
    }

    private fun showProfile() {
        profile_switcher.showFirst()
    }

    private fun showLoading() {
        profile_switcher.showSecond()
        profileLoading.showLoading()
    }

    private fun showError() {
        profile_switcher.showSecond()
        profileLoading.showError()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profileViewModel.lazyProfile.status.observe(this) {
            when (it) {
                LazyUser.Status.LOADING -> {
                    showLoading()
                }
                LazyUser.Status.TOKEN_DEPRECATED -> {
                    longToast(R.string.token_deprecated)
                    profileViewModel.logout()
                    startActivity<LoginActivity>()
                }
                LazyUser.Status.LOADED_FROM_DATABASE, LazyUser.Status.LOADED -> {
                    showProfile()
                }
                LazyUser.Status.SERVER_ERROR -> {
                    showError()
                }
            }
        }

        profileLoading.setOnRetryListener {
            profileViewModel.retry()
        }

        profileViewModel.lazyProfile.user.observe(this) {
            profile_name.text = it.name
            profile_surname.text = it.surname
            profile_login.text = it.login
            val birthday = it.birthdayString
            profile_birthday_container.isVisible = if (birthday == null) {
                false
            } else {
                profile_birthday.text = birthday
                true
            }
        }

        profile_logout.setOnClickListener {
            profileViewModel.logout()
            startActivity<LoginActivity>()
        }
    }


}