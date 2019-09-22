package ru.rpuxa.messenger.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.model.db.CurrentUser
import ru.rpuxa.messenger.model.db.CurrentUserDao
import ru.rpuxa.messenger.model.db.UsersDao
import ru.rpuxa.messenger.model.server.EditorFields
import ru.rpuxa.messenger.model.server.Error
import ru.rpuxa.messenger.model.server.LongOperationsServer
import ru.rpuxa.messenger.model.server.Server
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ProfileEditorViewModel @Inject constructor(
    private val currentUserDao: CurrentUserDao,
    private val usersDao: UsersDao,
    private val server: Server,
    private val longOperationsServer: LongOperationsServer,
    private val context: Context
) : ViewModel() {

    private val currentUser = runBlocking { currentUserDao.get() }

    val profile = runBlocking {
        val id = currentUser.id
        if (id == CurrentUser.ID_NOT_INITIALIZED) error("Not initialized user!")
        usersDao.load(id) ?: error("User not found in database")
    }

    val showPersonalAcceptButton: LiveData<Boolean> get() = _showPersonalAcceptButton
    val showEnterAcceptButton: LiveData<Boolean> get() = _showEnterAcceptButton
    val nameError: LiveData<String?> get() = _nameError
    val loginError: LiveData<String?> get() = _loginError
    val newPasswordError: LiveData<String?> get() = _newPasswordError
    val newRepeatPasswordError: LiveData<String?> get() = _newRepeatPasswordError
    val currentPasswordError: LiveData<Boolean> get() = _currentPasswordError
    val birthdayError: LiveData<String?> get() = _birthdayError
    val surnameError: LiveData<String?> get() = _surnameError
    val status: LiveData<Status> get() = _status

    lateinit var avatarFile: File
        private set


    private val _showPersonalAcceptButton = MutableLiveData(false)
    private val _showEnterAcceptButton = MutableLiveData(false)
    private val _nameError = MutableLiveData<String?>(null)
    private val _surnameError = MutableLiveData<String?>(null)
    private val _birthdayError = MutableLiveData<String?>(null)
    private val _currentPasswordError = MutableLiveData<Boolean>(false)
    private val _loginError = MutableLiveData<String?>(null)
    private val _newPasswordError = MutableLiveData<String?>(null)
    private val _newRepeatPasswordError = MutableLiveData<String?>(null)
    private val _status = MutableLiveData<Status>(Status.DONE)

    private inner class Fields : HashMap<EditorFields, Any>() {
        override fun put(key: EditorFields, value: Any): Any? {
            val put = super.put(key, value)
            update()
            return put
        }

        fun update() {
            _showPersonalAcceptButton.value = changedFields(true).isNotEmpty()
            _showEnterAcceptButton.value = changedFields(false).isNotEmpty()
        }
    }

    private val fieldsMap = Fields()

    fun resetStatus() {
        _status.value = Status.DONE
    }

    private fun changedFields(personal: Boolean): Map<String, String> {
        val newMap = HashMap<String, String>()
        loop@ for ((field, value) in fieldsMap) {

            val profileField: Any? = if (personal) {
                when (field) {
                    EditorFields.NAME -> profile.name
                    EditorFields.SURNAME -> profile.surname
                    EditorFields.BIRTHDAY -> profile.birthday
                    else -> continue@loop
                }
            } else {
                if (field == EditorFields.LOGIN) profile.login else continue
            }

            if (profileField != value)
                newMap[field.text] = value.toString()
        }

        return newMap
    }

    private fun acceptFields(fields: Map<String, String>) {
        _status.value = Status.CHANGING_DATA
        viewModelScope.launch {
            try {
                val answer = server.setInfo(currentUser.token, fields)

                if (answer.error == Error.UNKNOWN_USER_FIELD.code) error(Error.UNKNOWN_USER_FIELD)
                if (answer.error == Error.CURRENT_PASSWORD_NEEDED.code) error(Error.CURRENT_PASSWORD_NEEDED)

                if (answer.error == Error.CURRENT_PASSWORD_WRONG.code) {
                    _currentPasswordError.value = true
                    _status.value = Status.DONE
                    return@launch
                }



                _currentPasswordError.value = false

                var index = 0
                fields.entries.forEach { (field, value) ->
                    if (field == CURRENT_PASSWORD) return@forEach
                    val errorCode = answer.errors[index++]
                    val isNoError = errorCode == Error.NO_ERROR.code
                    val errorText = if (isNoError) null else answer.errorTexts[errorCode]
                    when (field) {
                        EditorFields.NAME.text -> {
                            _nameError.value = errorText
                            if (isNoError) profile.name = value
                        }

                        EditorFields.SURNAME.text -> {
                            _surnameError.value = errorText
                            if (isNoError) profile.surname = value
                        }

                        EditorFields.BIRTHDAY.text -> {
                            _birthdayError.value = errorText
                            if (isNoError) profile.birthday = value
                        }

                        EditorFields.LOGIN.text -> {
                            _loginError.value = errorText
                            if (isNoError) profile.login = value
                        }

                        EditorFields.PASSWORD.text -> {
                            _newPasswordError.value = errorText
                        }
                    }
                }
                fieldsMap.update()
                _status.value = Status.DONE
            } catch (e: IOException) {
                _status.value = Status.SERVER_ERROR
            }
        }
    }

    fun acceptPersonalFields() {
        val fields = changedFields(true)
        if (fields.isEmpty()) error("Empty fields")
        acceptFields(fields)
    }

    fun acceptEnterChanges(
        currentPassword: String,
        newPassword: String,
        newRepeatPassword: String
    ) {
        if (newPassword != newRepeatPassword) {
            _newRepeatPasswordError.value = context.getString(R.string.passwords_not_equals)
            return
        }
        _newRepeatPasswordError.value = null

        var fields = changedFields(false) + (CURRENT_PASSWORD to currentPassword)
        if (newPassword.isNotEmpty())
            fields = fields + (EditorFields.PASSWORD.text to newPassword)

        acceptFields(fields)
    }

    fun nameChanged(name: String) {
        fieldsMap[EditorFields.NAME] = name
    }

    fun surnameChanged(surname: String) {
        fieldsMap[EditorFields.SURNAME] = surname
    }

    fun birthdayChanged(birthday: String) {
        fieldsMap[EditorFields.BIRTHDAY] = birthday
    }

    fun loginChanged(login: String) {
        fieldsMap[EditorFields.LOGIN] = login
    }

    fun updateAvatar(avatar: File) {
        _status.value = Status.UPLOADING_ICON
        viewModelScope.launch {
            try {
                val fileReqBody = RequestBody.create(MediaType.parse("image/*"), avatar)
                val part = MultipartBody.Part.createFormData("upload", "avatar", fileReqBody)
                val answer = longOperationsServer.setAvatar(currentUser.token, part)
                val url = answer.url
                profile.avatar = url
                usersDao.insert(profile)
                avatarFile = avatar
                _status.value = Status.ICON_UPLOADED
            } catch (e: IOException) {
                _status.value = Status.SERVER_ERROR
            }
        }
    }

    enum class Status {
        CHANGING_DATA,
        UPLOADING_ICON,
        ICON_UPLOADED,
        SERVER_ERROR,
        DONE
    }

    companion object {
        private const val CURRENT_PASSWORD = "current_pass"
    }
}