package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.startActivity
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.lazyNavController
import ru.rpuxa.messenger.observe
import ru.rpuxa.messenger.view.MainActivity
import ru.rpuxa.messenger.view.dialogs.LoadingDialog
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.LoginViewModel

open class LoginFragment : Fragment() {

    private var loadingDialog: LoadingDialog? = null

    protected val loginViewModel: LoginViewModel by viewModel()
    protected val navController by lazyNavController

    protected open val loadingText get() = R.string.account_entering

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)

    protected fun observerStatus() {
        loginViewModel.resetStatus()

        loginViewModel.status.observe(this) {
            val error = when (it) {
                LoginViewModel.Status.PASSWORDS_NOT_EQUAL -> getString(R.string.passwords_not_equals)
                LoginViewModel.Status.WRONG_LOGIN_OR_PASSWORD -> getString(R.string.wrong_login_or_password)
                LoginViewModel.Status.SERVER_UNAVAILABLE -> getString(R.string.unreachable_server)
                LoginViewModel.Status.WRONG_REG_DATA -> loginViewModel.error.value
                else -> null
            }
            login_error.isVisible = if (error == null) {
                false
            } else {
                login_error.text = error
                true
            }

            loadingDialog?.dismiss()

            if (it == LoginViewModel.Status.SIGNING) {
                loadingDialog = LoadingDialog.show(childFragmentManager, getString(loadingText))
            }

            if (it == LoginViewModel.Status.LOGIN_SUCCESSFUL) {
                startActivity<MainActivity>()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        name_edit_text.isVisible = false
        surname_edit_text.isVisible = false
        repeat_password_edit_text.isVisible = false
        first_login_button.text = getText(R.string.sign_in)
        second_login_button.text = getText(R.string.sign_up)

        observerStatus()


        first_login_button.setOnClickListener {
            loginViewModel.login(
                login_edit_text.text.toString(),
                password_edit_text.text.toString()
            )
        }

        second_login_button.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegFragment())
        }
    }
}