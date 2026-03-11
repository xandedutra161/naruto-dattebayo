package com.example.dattebayoapp.data.repository

import com.example.dattebayoapp.data.local.dao.CharacterDao
import com.example.dattebayoapp.data.local.mapper.toEntity
import com.example.dattebayoapp.data.remote.mapper.toDomain
import com.example.dattebayoapp.data.remote.service.NarutoApiService
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val narutoApiService: NarutoApiService,
    private val characterDao: CharacterDao,
) : CharacterRepository {

    override suspend fun getCharacters(): CharacterPage {
        val favoriteIds = characterDao.getFavoriteCharacterIds().toSet()
        val existingCharacters = characterDao.getCharacters().associateBy { it.id }
        val page = narutoApiService.getCharacters().toDomain()
        val items = page.items.map { item ->
            item.copy(isFavorite = item.id in favoriteIds)
        }

        characterDao.upsertCharacters(
            items.map { item -> item.toEntity(existingCharacters[item.id]) },
        )

        return page.copy(items = items)
    }

    override suspend fun getCharacterDetails(id: Int): CharacterDetails {
        val existingCharacter = characterDao.getCharacterById(id)
        val details = narutoApiService.getCharacterDetails(id)
            .toDomain()
            .copy(isFavorite = existingCharacter?.isFavorite ?: false)

        characterDao.upsertCharacter(details.toEntity(existingCharacter))

        return details
    }

    override suspend fun toggleFavorite(id: Int): Boolean {
        val existingCharacter = characterDao.getCharacterById(id)

        if (existingCharacter != null) {
            val newFavoriteState = !existingCharacter.isFavorite
            characterDao.updateFavorite(id, newFavoriteState)
            return newFavoriteState
        }

        val details = narutoApiService.getCharacterDetails(id).toDomain().copy(isFavorite = true)
        characterDao.upsertCharacter(details.toEntity())
        return true
    }
}
