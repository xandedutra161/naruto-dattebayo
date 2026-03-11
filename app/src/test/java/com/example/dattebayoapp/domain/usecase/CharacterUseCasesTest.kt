package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.repository.CharacterRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterUseCasesTest {

    @Test
    fun `GetCharactersUseCase delegates to repository`() = runTest {
        val repository = FakeCharacterRepository()

        val result = GetCharactersUseCase(repository).invoke()

        assertEquals(repository.page, result)
    }

    @Test
    fun `GetCharacterDetailUseCase delegates to repository`() = runTest {
        val repository = FakeCharacterRepository()

        val result = GetCharacterDetailUseCase(repository).invoke(1344)

        assertEquals(repository.details, result)
        assertEquals(1344, repository.lastDetailId)
    }

    @Test
    fun `GetFavoriteCharactersUseCase delegates to repository`() = runTest {
        val repository = FakeCharacterRepository()

        val result = GetFavoriteCharactersUseCase(repository).invoke()

        assertEquals(repository.favoriteCharacters, result)
    }

    @Test
    fun `SaveFavoriteUseCase delegates to repository`() = runTest {
        val repository = FakeCharacterRepository()

        val result = SaveFavoriteUseCase(repository).invoke(1344)

        assertTrue(result)
        assertEquals(1344, repository.lastSavedFavoriteId)
    }

    @Test
    fun `RemoveFavoriteUseCase delegates to repository`() = runTest {
        val repository = FakeCharacterRepository()

        val result = RemoveFavoriteUseCase(repository).invoke(1344)

        assertTrue(result)
        assertEquals(1344, repository.lastRemovedFavoriteId)
    }

    @Test
    fun `ToggleFavoriteUseCase delegates to repository`() = runTest {
        val repository = FakeCharacterRepository()

        val result = ToggleFavoriteUseCase(repository).invoke(1344)

        assertTrue(result)
        assertEquals(1344, repository.lastToggledFavoriteId)
    }

    private class FakeCharacterRepository : CharacterRepository {
        val page = CharacterPage(
            items = listOf(
                CharacterListItem(
                    id = 1344,
                    name = "Naruto Uzumaki",
                    images = listOf("image-1"),
                    debut = CharacterDebut(manga = "Naruto Chapter #1"),
                ),
            ),
            currentPage = 1,
            pageSize = 20,
            total = 1431,
        )

        val details = CharacterDetails(
            id = 1344,
            name = "Naruto Uzumaki",
            images = listOf("image-1", "image-2"),
            debut = CharacterDebut(
                manga = "Naruto Chapter #1",
                anime = "Naruto Episode #1",
            ),
            isFavorite = true,
        )

        val favoriteCharacters = listOf(
            CharacterListItem(
                id = 1344,
                name = "Naruto Uzumaki",
                images = listOf("image-1"),
                debut = CharacterDebut(manga = "Naruto Chapter #1"),
                isFavorite = true,
            ),
        )

        var lastDetailId: Int? = null
        var lastSavedFavoriteId: Int? = null
        var lastRemovedFavoriteId: Int? = null
        var lastToggledFavoriteId: Int? = null

        override suspend fun getCharacters(): CharacterPage = page

        override suspend fun getCharacterDetails(id: Int): CharacterDetails {
            lastDetailId = id
            return details
        }

        override suspend fun getFavoriteCharacters(): List<CharacterListItem> = favoriteCharacters

        override suspend fun saveFavorite(id: Int): Boolean {
            lastSavedFavoriteId = id
            return true
        }

        override suspend fun removeFavorite(id: Int): Boolean {
            lastRemovedFavoriteId = id
            return true
        }

        override suspend fun toggleFavorite(id: Int): Boolean {
            lastToggledFavoriteId = id
            return true
        }
    }
}
