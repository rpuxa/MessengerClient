package ru.rpuxa.messenger.model.server.answers

import com.google.gson.annotations.SerializedName

open class Answer {
    var error: Int = 0

    @SerializedName("error_text")
    lateinit var errorText: String
}
