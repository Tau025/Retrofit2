package com.devtau.retrofit2.voice

import com.devtau.retrofit2.voice.request.OpenSessionBody
import com.devtau.retrofit2.voice.request.RecognitionRequestBody
import com.devtau.retrofit2.voice.response.OpenSessionResponse
import com.devtau.retrofit2.voice.response.Word
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

internal interface BackendAPIVoice {

    @POST("vksession/rest/session")
    fun openSession(
        @Body body: OpenSessionBody
    ): Call<OpenSessionResponse>

    @POST("vkasr/rest/v1/recognize/words")
    fun recognizeFile(
        @Body body: RecognitionRequestBody,
        @Header("X-Session-ID") sessionId: String,
    ): Call<List<Word>>

    companion object {
        const val API_BASE_URL = "https://vkplatform.speechpro.com/"
    }
}