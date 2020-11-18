package com.devtau.retrofit2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface BackendAPI {

	String API_BASE_URL = "https://api.github.com/";//Base URL: always ends with /
	String API_PRECISE_URL = "users/{user}";//@Url: DO NOT start with /

	@GET(API_PRECISE_URL)
	Call<GitModelPOJO> executeRequest(@Path("user") String user);
}
