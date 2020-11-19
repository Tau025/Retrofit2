package com.devtau.retrofit2.voice.response

import com.google.gson.annotations.SerializedName

class Word(
    val word: String,
    val score: String,
    val speaker: Int?,
    val begin: String?,
    val length: String?,
    @SerializedName("stop_sign")
    val stopSign: String?
)