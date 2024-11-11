package com.dicoding.asclepius.data.repository

import com.dicoding.asclepius.data.remote.retrofit.ArticleServices

class ArticleRepository(private val apiServices: ArticleServices) {
    suspend fun getArticleCancer() = apiServices.getArticleCancer()
}