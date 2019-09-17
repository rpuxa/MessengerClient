package ru.rpuxa.messenger.model.server.answers

import com.google.gson.annotations.SerializedName

class SetInfoAnswer(
    val errors: List<Int>,
    @SerializedName("error_texts")
    val errorTexts: Map<Int, String>
) : Answer()