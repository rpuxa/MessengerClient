package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.showFirst
import ru.rpuxa.messenger.showSecond

class LoadingFragment : Fragment() {

    private var onRetryListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_loading, container, false)

        view.setOnClickListener {
            showLoading()
            onRetryListener?.invoke()
        }

        return view
    }

    fun showError() {
        (view as ViewSwitcher).showFirst()
    }

    fun showLoading() {
        (view as ViewSwitcher).showSecond()
    }

    fun setOnRetryListener(listener: () -> Unit) {
        onRetryListener = listener
    }
}