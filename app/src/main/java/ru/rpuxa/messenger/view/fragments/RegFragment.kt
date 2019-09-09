package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.lazyNavController
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.LoginViewModel

class RegFragment : Fragment() {

    val loginViewModel: LoginViewModel by viewModel()
    private val navController by lazyNavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_reg, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sign_up.setOnClickListener {
            navController.popBackStack()
        }
    }
}