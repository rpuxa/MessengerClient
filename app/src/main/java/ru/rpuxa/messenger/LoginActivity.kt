package ru.rpuxa.messenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        content.alpha = 0f

        content.animate()
            .setDuration(1000)
            .alpha(1f)
            .start()
    }
}
