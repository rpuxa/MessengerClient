package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_login.*
import ru.rpuxa.messenger.R

class RegFragment : LoginFragment() {

    override val loadingText: Int get() = R.string.registrating

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observerStatus()

        first_login_button.setOnClickListener {
            loginViewModel.register(
                name_edit_text.text.toString(),
                surname_edit_text.text.toString(),
                login_edit_text.text.toString(),
                password_edit_text.text.toString(),
                repeat_password_edit_text.text.toString()
            )
        }

        second_login_button.setOnClickListener {
            navController.popBackStack()
        }
    }
}