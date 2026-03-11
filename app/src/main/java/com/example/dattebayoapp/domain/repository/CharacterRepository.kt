package com.example.dattebayoapp.domain.repository

import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.model.CharacterPage

interface CharacterRepository {
    suspend fun getCharacters(): CharacterPage

    suspend fun getCharacterDetails(id: Int): CharacterDetails

    suspend fun getFavoriteCharacters(): List<CharacterListItem>

    suspend fun saveFavorite(id: Int): Boolean

    suspend fun removeFavorite(id: Int): Boolean

    suspend fun toggleFavorite(id: Int): Boolean
}
