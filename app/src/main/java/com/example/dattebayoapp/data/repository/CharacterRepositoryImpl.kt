package com.example.dattebayoapp.data.repository

import com.example.dattebayoapp.data.remote.mapper.toDomain
import com.example.dattebayoapp.data.remote.service.NarutoApiService
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val narutoApiService: NarutoApiService,
) : CharacterRepository {

    override suspend fun getCharacters(): CharacterPage {
        return narutoApiService.getCharacters().toDomain()
    }

    override suspend fun getCharacterDetails(id: Int): CharacterDetails {
        return narutoApiService.getCharacterDetails(id).toDomain()
    }
}
