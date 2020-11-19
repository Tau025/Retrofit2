package com.devtau.retrofit2.voice

import com.devtau.retrofit2.voice.request.OpenSessionBody
import com.devtau.retrofit2.voice.request.RecognitionRequestBody
import com.devtau.retrofit2.voice.response.BaseCallback
import com.devtau.retrofit2.voice.response.OpenSessionResponse
import com.devtau.retrofit2.voice.response.Word
import com.google.gson.GsonBuilder
import io.reactivex.functions.Consumer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
/**
 * library authors page: http://square.github.io/retrofit/
 */
class RESTClientVoice(private val view: VoiceRecognitionActivityView) {

    private var httpClientLogging = buildClient(true)
    private var httpClientNotLogging = buildClient(false)

    fun openSession(listener: Consumer<String>) {
        Timber.d("openSession")
        view.showProgress(true)

        getBackendApiClient(true)
            .openSession(OpenSessionBody()).enqueue(object: BaseCallback<OpenSessionResponse>(view) {
                override fun processBody(responseBody: OpenSessionResponse?) {
                    Timber.d("recognizeFile success. responseBody=$responseBody")
                    view.showProgress(false)
                    view.showMsg("Session opened. sessionId is ${responseBody?.sessionId}")
                    listener.accept(responseBody?.sessionId.orEmpty())
                }
            })
    }

    fun recognizeFile(file: RecognitionRequestBody.AudioFile, sessionId: String) {
        Timber.d("recognizeFile")
        view.showProgress(true)

        getBackendApiClient(true)
            .recognizeFile(RecognitionRequestBody(file), sessionId)
            .enqueue(object: BaseCallback<List<Word>>(view) {
                override fun processBody(responseBody: List<Word>?) {
                    Timber.d("recognizeFile success. responseBody=$responseBody")
                    view.showProgress(false)
                    view.showMsg("File recognized. " +
                        "responseBody contains ${responseBody?.size} elements.\n " +
                        "${responseBody?.joinToString { it.word }}")
                }
            })
    }

    private fun buildClient(loggerNeeded: Boolean): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
        if (loggerNeeded) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder.build()
    }

    private fun getBackendApiClient(loggerNeeded: Boolean): BackendAPIVoice = Retrofit.Builder()
        .baseUrl(BackendAPIVoice.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .client(if (loggerNeeded) httpClientLogging else httpClientNotLogging)
        .build()
        .create(BackendAPIVoice::class.java)

    companion object {
        private const val TIMEOUT_CONNECT = 10L
        private const val TIMEOUT_READ = 60L
        private const val TIMEOUT_WRITE = 120L
    }
}