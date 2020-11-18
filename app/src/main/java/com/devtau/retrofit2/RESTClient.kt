package com.devtau.retrofit2

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
/**
 * library authors page: http://square.github.io/retrofit/
 */
class RESTClient(private val view: MainActivityView) {

    private var httpClientLogging = buildClient(true)
    private var httpClientNotLogging = buildClient(false)


    fun sendRequest(userName: String?): Boolean {
        Log.d(LOG_TAG, "entered retrofit sendRequest method")
        if (userName.isNullOrEmpty()) {
            Log.d(LOG_TAG, "error. user cannot be null or empty")
            return false
        }
        view.showProgress(true)

        getBackendApiClient(true)
            .getUserData(userName)?.enqueue(object: Callback<GitUserModel?> {
                override fun onResponse(call: Call<GitUserModel?>, response: Response<GitUserModel?>) {
                    view.showProgress(false)
                    if (response.isSuccessful) {
                        Log.d(LOG_TAG, "retrofit response isSuccessful")
                        handleSuccess(response.body())
                    } else {
                        //ответ пришел, но говорит об ошибке
                        val errorCode = response.code()
                        Log.d(LOG_TAG, "retrofit response is not successful. errorCode: $errorCode")
                        Log.d(LOG_TAG, "check API_PRECISE_URL and executeRequest parameters if any")
                        val errorBody = response.errorBody()
                        handleError(errorBody)
                    }
                }

                override fun onFailure(call: Call<GitUserModel?>, t: Throwable) {
                    Log.e(LOG_TAG, "retrofit failure: " + t.localizedMessage)
                    Log.e(LOG_TAG, "check API_BASE_URL and internet connection")
                    view.showProgress(false)
                    handleFailure(t.localizedMessage)
                }
            })
        return true
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

    private fun getBackendApiClient(loggerNeeded: Boolean): BackendAPI = Retrofit.Builder()
        .baseUrl(BackendAPI.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .client(if (loggerNeeded) httpClientLogging else httpClientNotLogging)
        .build()
        .create(BackendAPI::class.java)

    private fun handleSuccess(user: GitUserModel?) = if (user == null) {
        view.showError("received null user")
        view.clearUserInfo()
    } else {
        view.showUserInfo(user)
        view.clearError()
    }

    private fun handleError(errorBody: ResponseBody?) {
        var errorMessage = "error"
        try {
            errorMessage = errorBody!!.string()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        view.clearUserInfo()
        view.showError(errorMessage)
    }

    private fun handleFailure(failureMessage: String) {
        view.clearUserInfo()
        view.showError(failureMessage)
    }

    companion object {
        private const val LOG_TAG = "RESTClient"
        private const val TIMEOUT_CONNECT = 10L
        private const val TIMEOUT_READ = 60L
        private const val TIMEOUT_WRITE = 120L
    }
}