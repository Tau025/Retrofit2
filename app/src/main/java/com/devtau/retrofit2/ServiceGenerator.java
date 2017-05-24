package com.devtau.retrofit2;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * является заменой старого RestAdapter
 */
public class ServiceGenerator {

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BackendAPI.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(new OkHttpClient()).build();
        return retrofit.create(serviceClass);
    }
}
