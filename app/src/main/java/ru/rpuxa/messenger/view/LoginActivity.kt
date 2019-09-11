package ru.rpuxa.messenger.view

import android.animation.AnimatorInflater
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.lazyNavController
import ru.rpuxa.messenger.observe
import ru.rpuxa.messenger.view.dialogs.LoadingDialog
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {


    private val loginViewModel: LoginViewModel by viewModel()
    private var loadingDialog: LoadingDialog? = null
    private var isRegistration = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        content.alpha = 0f

        content.animate()
            .setDuration(1000)
            .alpha(1f)
            .start()


        loginViewModel.status.observe(this) {
            login_edit_text.error = when (it) {
                LoginViewModel.Status.WRONG_LOGIN_OR_PASSWORD -> "Неверный логин или пароль"
                LoginViewModel.Status.SERVER_UNAVAILABLE -> "Сервер недоступен"
                else -> null
            }

            if (it == LoginViewModel.Status.SIGNING) {
                Log.d("MyDebug", "SIGNING")
                loadingDialog?.dismiss()
                loadingDialog = LoadingDialog.show(supportFragmentManager, "Входим в аккаунт")
            } else {
                Log.d("MyDebug", "NOT SIGNING $loadingDialog")
                loadingDialog?.dismiss()
            }

            if (it == LoginViewModel.Status.LOGIN_SUCCESSFUL) {
                //TODO
                toast("Успешный вход")
            }
        }

        first_reg_button.setOnClickListener {
            val login = login_edit_text.text.toString()
            val password = password_edit_text.text.toString()
            if (isRegistration) {
                loginViewModel.register(
                    name_edit_text.text.toString(),
                    surname_edit_text.text.toString(),
                    login,
                    password
                )
            } else {
                loginViewModel.login(
                    login,
                    password
                )
            }
        }

        second_reg_button.setOnClickListener {
            //navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegFragment())
            val views = arrayOf(name_edit_text, surname_edit_text, repeat_password_edit_text)
            views.forEach {
                val anim = AnimatorInflater.loadAnimator(this, R.animator.disappear_view)
                anim.setTarget(it)
                anim.start()
            }
            isRegistration = !isRegistration
        }
        TransitionManager.beginDelayedTransition(content, AutoTransition())
    }
}

