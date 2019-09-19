package ru.rpuxa.messenger.view.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.activity_profile_editor.*
import org.jetbrains.anko.longToast
import ru.rpuxa.messenger.view.dialogs.LoadingDialog
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.ProfileEditorViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore
import android.database.Cursor
import android.net.Uri
import ru.rpuxa.messenger.R
import java.io.File
import java.io.FileOutputStream


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

        profile_editor_login.addTextChangedListener {
            viewModel.loginChanged(it!!.toString())
        }

        viewModel.nameError.observe(this, profile_editor_name_layout::setError)
        viewModel.surnameError.observe(this, profile_editor_surname_layout::setError)
        viewModel.birthdayError.observe(
            this,
            profile_editor_birthday::setError
        )
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

                ProfileEditorViewModel.Status.CHANGING_DATA -> {
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

        /* viewModel.showEnterAcceptButton.observe(this) {
             profile_editor_accept_enter.isVisible = it
         }*/

        profile_editor_accept_enter.setOnClickListener {
            viewModel.acceptEnterChanges(
                profile_editor_current_pass.text!!.toString(),
                profile_editor_pass.text!!.toString(),
                profile_editor_repeat_pass.text!!.toString()
            )
        }

        profile_editor_birthday.setOnClickListener {
            val today = Date()
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val birthday = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(
                        Date(
                            year - 1900,
                            month,
                            dayOfMonth
                        )
                    )
                    profile_editor_birthday.setText(birthday, null)
                    viewModel.birthdayChanged(birthday)
                },
                today.year + 1900,
                today.month,
                today.day
            ).show()
        }

        profile_editor_change_icon.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(
                Intent.createChooser(intent, "Select a avatar"),
                CHOSE_AVATAR_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOSE_AVATAR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
             data?.data?.let {

                 val inputStream = contentResolver.openInputStream(it)!!

                 val file = File(cacheDir, "tmp_avatar")

                 inputStream.use { input ->
                     val outputStream = FileOutputStream(file)
                     outputStream.use { output ->
                         val buffer = ByteArray(4 * 1024) // buffer size
                         while (true) {
                             val byteCount = input.read(buffer)
                             if (byteCount < 0) break
                             output.write(buffer, 0, byteCount)
                         }
                         output.flush()
                     }
                 }

                 viewModel.updateAvatar(file)
            }
        }
    }

    fun getRealPathFromURI(contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val CHOSE_AVATAR_REQUEST_CODE = 97
    }
}
