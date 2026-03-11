package com.example.dattebayoapp.data.remote.service

import com.example.dattebayoapp.data.remote.dto.CharacterDetailsDto
import com.example.dattebayoapp.data.remote.dto.CharactersResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface NarutoApiService {
    @GET("characters")
    suspend fun getCharacters(): CharactersResponseDto

    @GET("characters/{id}")
    suspend fun getCharacterDetails(@Path("id") id: Int): CharacterDetailsDto
}
