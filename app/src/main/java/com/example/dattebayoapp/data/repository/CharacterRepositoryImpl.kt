package com.example.dattebayoapp.data.repository

import com.example.dattebayoapp.data.local.dao.CharacterDao
import com.example.dattebayoapp.data.local.mapper.toEntity
import com.example.dattebayoapp.data.local.mapper.toListItem
import com.example.dattebayoapp.data.remote.mapper.toDomain
import com.example.dattebayoapp.data.remote.service.NarutoApiService
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem
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

    override suspend fun getFavoriteCharacters(): List<CharacterListItem> {
        return characterDao.getFavoriteCharacters().map { entity -> entity.toListItem() }
    }

    override suspend fun saveFavorite(id: Int): Boolean {
        val existingCharacter = characterDao.getCharacterById(id)

        if (existingCharacter != null) {
            characterDao.updateFavorite(id, true)
            return true
        }

        val details = narutoApiService.getCharacterDetails(id).toDomain().copy(isFavorite = true)
        characterDao.upsertCharacter(details.toEntity())
        return true
    }

    override suspend fun removeFavorite(id: Int): Boolean {
        val existingCharacter = characterDao.getCharacterById(id) ?: return false
        characterDao.updateFavorite(id, false)
        return existingCharacter.isFavorite
    }

    override suspend fun toggleFavorite(id: Int): Boolean {
        val existingCharacter = characterDao.getCharacterById(id)

        if (existingCharacter != null) {
            return if (existingCharacter.isFavorite) {
                removeFavorite(id)
                false
            } else {
                saveFavorite(id)
            }
        }

        return saveFavorite(id)
    }
}
