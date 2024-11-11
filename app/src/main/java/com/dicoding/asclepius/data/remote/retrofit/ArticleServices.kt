package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.data.remote.response.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleServices {

    @GET("everything")
    suspend fun getArticleCancer(
        @Query("q") query: String = "flood",
    ): Response
}