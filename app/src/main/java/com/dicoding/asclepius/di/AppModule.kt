package com.dicoding.asclepius.di

import com.dicoding.asclepius.data.remote.retrofit.ArticleServices
import com.dicoding.asclepius.data.remote.retrofit.RetrofitClient
import com.dicoding.asclepius.data.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiConfig(): RetrofitClient {
        return RetrofitClient()
    }

    @Provides
    @Singleton
    fun provideArticleServices(apiConfig: RetrofitClient): ArticleServices {
        return apiConfig.getApiService()
    }

    @Provides
    @Singleton
    fun provideArticleRepository(apiServices: ArticleServices): ArticleRepository {
        return ArticleRepository(apiServices)
    }
}