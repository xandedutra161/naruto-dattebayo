package com.example.dattebayoapp.core.di

import com.example.dattebayoapp.core.network.RetrofitClient
import com.example.dattebayoapp.data.remote.service.NarutoApiService
import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single<Gson> { RetrofitClient.createGson() }
    single<Retrofit> { RetrofitClient.createRetrofit(get()) }
    single<NarutoApiService> { RetrofitClient.createNarutoApiService(get()) }
}
