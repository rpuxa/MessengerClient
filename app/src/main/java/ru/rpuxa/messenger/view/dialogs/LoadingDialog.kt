package ru.rpuxa.messenger.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_loading.*
import ru.rpuxa.messenger.R

class LoadingDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_loading, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loading_dialog_text.text = arguments?.get(TEXT) as? String ?: error("Text needed")
    }

    companion object {
        private const val TEXT = "text"

        fun show(fm: FragmentManager, text: String): LoadingDialog {
            val loadingDialog = LoadingDialog()
            loadingDialog.arguments = bundleOf(TEXT to text)
            loadingDialog.show(fm, "loadingdialog")
            return loadingDialog
        }
    }
}