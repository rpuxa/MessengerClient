package ru.rpuxa.messenger.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.activity_profile_editor.*
import org.jetbrains.anko.longToast
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.view.dialogs.LoadingDialog
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.ProfileEditorViewModel

class ProfileEditorActivity : AppCompatActivity() {

    private val viewModel: ProfileEditorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_editor)

        setSupportActionBar(profile_editor_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val profile = viewModel.profile

        profile_editor_name.setText(profile.name, null)
        profile_editor_surname.setText(profile.surname, null)
        profile_editor_birthday.setText(profile.birthday ?: "", null)
        profile_editor_login.setText(profile.login, null)

        profile_editor_name.addTextChangedListener {
            viewModel.nameChanged(it!!.toString())
        }

        profile_editor_surname.addTextChangedListener {
            viewModel.surnameChanged(it!!.toString())
        }

        profile_editor_birthday.addTextChangedListener {
            viewModel.birthdayChanged(it!!.toString())
        }

        viewModel.nameError.observe(this, profile_editor_name_layout::setError)
        viewModel.surnameError.observe(this, profile_editor_surname_layout::setError)
        viewModel.birthdayError.observe(
            this,
            profile_editor_birthday::setError
        ) //TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        viewModel.loginError.observe(this, profile_editor_login_layout::setError)
        viewModel.currentPasswordError.observe(this) {
            profile_editor_current_pass_layout.error =
                if (it) getString(R.string.wrong_password) else null
        }
        viewModel.newPasswordError.observe(this, profile_editor_pass_layout::setError)
        viewModel.newRepeatPasswordError.observe(this, profile_editor_repeat_pass_layout::setError)

        var dialog: LoadingDialog? = null
        viewModel.status.observe(this) {
            dialog?.dismiss()

            when (it) {
                ProfileEditorViewModel.Status.DONE -> {
                    // nothing
                }

                ProfileEditorViewModel.Status.SERVER_ERROR -> {
                    longToast(R.string.unreachable_server)
                    viewModel.resetStatus()
                }

                ProfileEditorViewModel.Status.LOADING -> {
                    dialog = LoadingDialog.show(
                        supportFragmentManager,
                        getString(R.string.accepting_changes)
                    )
                }
            }
        }

        viewModel.showPersonalAcceptButton.observe(this) {
            profile_editor_personal_buttons.isVisible = it
        }

        profile_editor_accept_personal.setOnClickListener {
            viewModel.acceptPersonalFields()
        }

        viewModel.showEnterAcceptButton.observe(this) {
            profile_editor_accept_enter.isVisible = it
        }

        profile_editor_accept_enter.setOnClickListener {
            viewModel.acceptEnterChanges(
                profile_editor_current_pass.text!!.toString(),
                profile_editor_pass.text!!.toString(),
                profile_editor_repeat_pass.text!!.toString()
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
