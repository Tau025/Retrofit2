package com.devtau.retrofit2.voice

import com.devtau.retrofit2.voice.request.OpenSessionBody
import com.devtau.retrofit2.voice.request.FileRecognitionRequestBody
import com.devtau.retrofit2.voice.request.StreamRecognitionRequestBody
import com.devtau.retrofit2.voice.response.OpenSessionResponse
import com.devtau.retrofit2.voice.response.RecognizeStreamResponse
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
        @Body body: FileRecognitionRequestBody,
        @Header("X-Session-ID") sessionId: String,
    ): Call<List<Word>>

    @POST("vkasr/rest/v1/recognize/stream")
    fun recognizeStream(
        @Body body: StreamRecognitionRequestBody,
        @Header("X-Session-ID") sessionId: String,
    ): Call<RecognizeStreamResponse>

    companion object {
        const val API_BASE_URL = "https://vkplatform.speechpro.com/"
    }
}