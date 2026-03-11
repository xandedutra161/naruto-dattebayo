package com.example.dattebayoapp.core.di

import com.example.dattebayoapp.core.network.RetrofitClient
import com.example.dattebayoapp.data.remote.service.NarutoApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = RetrofitClient.createGson()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = RetrofitClient.createRetrofit(gson)

    @Provides
    @Singleton
    fun provideNarutoApiService(retrofit: Retrofit): NarutoApiService {
        return RetrofitClient.createNarutoApiService(retrofit)
    }
}
