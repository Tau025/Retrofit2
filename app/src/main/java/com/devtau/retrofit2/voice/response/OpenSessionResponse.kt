package com.devtau.retrofit2.voice.response

import com.google.gson.annotations.SerializedName

class OpenSessionResponse(
    @SerializedName("session_id")
    val sessionId: String
)