package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dialogs.*
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.view.adapters.DialogsAdapter
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.DialogsViewModel

class DialogsFragment : Fragment() {

    private val viewModel: DialogsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dialogs, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DialogsAdapter(this)
        dialogs_list.adapter = adapter
        dialogs_list.layoutManager = LinearLayoutManager(requireContext())

        viewModel.dialogs.observe(this) {
            adapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
    }
}