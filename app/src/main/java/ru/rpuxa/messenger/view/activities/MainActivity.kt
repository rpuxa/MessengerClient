package ru.rpuxa.messenger.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.content)
        nav_view.setupWithNavController(navController)

        viewModel.loadLastActionId()
    }

    override fun onBackPressed() {
    }
}
