package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    fun getApiService(): ArticleServices {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ArticleServices::class.java)
    }
}