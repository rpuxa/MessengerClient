package ru.rpuxa.messenger.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (loginViewModel.isUserAuthorized()) {
            startActivity<MainActivity>()
            return
        }

        setContentView(R.layout.activity_login)

        content.alpha = 0f

        content.animate()
            .setDuration(1000)
            .alpha(1f)
            .start()
    }

}

