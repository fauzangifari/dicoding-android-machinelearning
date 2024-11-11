package com.dicoding.asclepius.data.remote.retrofit

import android.util.Log
import com.dicoding.asclepius.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.APIKEY}")
            .method(original.method, original.body)
            .build()
        Log.d("ApiKeyInterceptor", "Authorization header: ${request.header("Authorization")}")
        return chain.proceed(request)
    }
}