package com.example.dattebayoapp.core.network

import com.example.dattebayoapp.data.remote.service.NarutoApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "https://dattebayo-api.onrender.com/"

    fun createGson(): Gson = GsonBuilder().create()

    fun createRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun createNarutoApiService(retrofit: Retrofit): NarutoApiService {
        return retrofit.create(NarutoApiService::class.java)
    }
}
