package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.rpuxa.messenger.R

class DialogsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dialogs, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}