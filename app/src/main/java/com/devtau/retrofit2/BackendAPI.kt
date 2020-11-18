package com.devtau.retrofit2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface BackendAPI {

    @GET("users/{user}")
    fun getUserData(
        @Path("user") userName: String
    ): Call<GitUserModel?>?

    companion object {
        const val API_BASE_URL = "https://api.github.com/" //Base URL: always ends with /
    }
}