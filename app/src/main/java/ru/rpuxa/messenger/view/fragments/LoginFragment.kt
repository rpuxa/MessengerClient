package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.toast
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.lazyNavController
import ru.rpuxa.messenger.observe
import ru.rpuxa.messenger.view.dialogs.LoadingDialog
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()
    private val navController by lazyNavController
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loginViewModel.status.observe(this) {
            login_edit_text.error = when (it) {
                LoginViewModel.Status.WRONG_LOGIN_OR_PASSWORD -> "Неверный логин или пароль"
                LoginViewModel.Status.SERVER_UNAVAILABLE -> "Сервер недоступен"
                else -> null
            }

            if (it == LoginViewModel.Status.SIGNING) {
                loadingDialog = LoadingDialog.show(childFragmentManager, "Входим в аккаунт")
            } else {
                loadingDialog?.dismiss()
            }

            if (it == LoginViewModel.Status.LOGIN_SUCCSESSFULL) {
                //TODO
                toast("Успешный вход")
            }
        }

        sign_in.setOnClickListener {
            loginViewModel.login(
                login_edit_text.text.toString(),
                password_edit_text.text.toString()
            )
        }

        sign_up.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegFragment())
        }
    }
}