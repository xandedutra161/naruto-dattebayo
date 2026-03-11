package com.example.dattebayoapp.data.repository

import com.example.dattebayoapp.data.local.dao.CharacterDao
import com.example.dattebayoapp.data.local.entity.CharacterEntity
import com.example.dattebayoapp.data.remote.dto.CharacterDebutDto
import com.example.dattebayoapp.data.remote.dto.CharacterDetailsDto
import com.example.dattebayoapp.data.remote.dto.CharacterPersonalDto
import com.example.dattebayoapp.data.remote.dto.CharacterRankDto
import com.example.dattebayoapp.data.remote.dto.CharacterSummaryDto
import com.example.dattebayoapp.data.remote.dto.CharactersResponseDto
import com.example.dattebayoapp.data.remote.service.NarutoApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterRepositoryImplTest {

    @Test
    fun `getCharacters returns mapped domain page`() = runTest {
        val repository = CharacterRepositoryImpl(
            narutoApiService = FakeNarutoApiService(),
            characterDao = FakeCharacterDao(),
        )

        val result = repository.getCharacters()

        assertEquals(1, result.currentPage)
        assertEquals(20, result.pageSize)
        assertEquals(1431, result.total)
        assertEquals(1, result.items.size)
        assertEquals("Naruto Uzumaki", result.items.first().name)
        assertEquals("Naruto Chapter #1", result.items.first().debut?.manga)
    }

    @Test
    fun `getCharacterDetails returns mapped domain details`() = runTest {
        val repository = CharacterRepositoryImpl(
            narutoApiService = FakeNarutoApiService(),
            characterDao = FakeCharacterDao(),
        )

        val result = repository.getCharacterDetails(id = 1344)

        assertEquals(1344, result.id)
        assertEquals("Naruto Uzumaki", result.name)
        assertEquals("Minato Namikaze", result.family["father"])
        assertEquals(listOf("Jinchuriki"), result.personal.classification)
        assertEquals(mapOf("Part I" to "Genin"), result.rank.ninjaRank)
        assertEquals(false, result.isFavorite)
    }

    @Test
    fun `toggleFavorite toggles cached character state`() = runTest {
        val characterDao = FakeCharacterDao(
            initialCharacters = mutableMapOf(
                1344 to CharacterEntity(
                    id = 1344,
                    name = "Naruto Uzumaki",
                    isFavorite = false,
                ),
            ),
        )
        val repository = CharacterRepositoryImpl(
            narutoApiService = FakeNarutoApiService(),
            characterDao = characterDao,
        )

        val result = repository.toggleFavorite(1344)

        assertEquals(true, result)
        assertEquals(true, characterDao.getCharacterById(1344)?.isFavorite)
    }

    @Test
    fun `toggleFavorite caches remote detail when character is not local yet`() = runTest {
        val characterDao = FakeCharacterDao()
        val repository = CharacterRepositoryImpl(
            narutoApiService = FakeNarutoApiService(),
            characterDao = characterDao,
        )

        val result = repository.toggleFavorite(1344)

        assertEquals(true, result)
        assertEquals(true, characterDao.getCharacterById(1344)?.isFavorite)
    }

    private class FakeNarutoApiService : NarutoApiService {
        override suspend fun getCharacters(): CharactersResponseDto {
            return CharactersResponseDto(
                characters = listOf(
                    CharacterSummaryDto(
                        id = 1344,
                        name = "Naruto Uzumaki",
                        images = listOf("image-1"),
                        debut = CharacterDebutDto(manga = "Naruto Chapter #1"),
                    ),
                ),
                currentPage = 1,
                pageSize = 20,
                total = 1431,
            )
        }

        override suspend fun getCharacterDetails(id: Int): CharacterDetailsDto {
            return CharacterDetailsDto(
                id = id,
                name = "Naruto Uzumaki",
                images = listOf("image-1", "image-2"),
                family = mapOf("father" to "Minato Namikaze"),
                personal = CharacterPersonalDto(
                    classification = com.google.gson.JsonParser.parseString("[\"Jinchuriki\"]"),
                ),
                rank = CharacterRankDto(
                    ninjaRank = mapOf("Part I" to "Genin"),
                    ninjaRegistration = "012607",
                ),
            )
        }
    }

    private class FakeCharacterDao(
        initialCharacters: MutableMap<Int, CharacterEntity> = mutableMapOf(),
    ) : CharacterDao {
        private val characters = initialCharacters

        override suspend fun getCharacters(): List<CharacterEntity> {
            return characters.values.sortedBy { it.id }
        }

        override suspend fun getCharacterById(id: Int): CharacterEntity? {
            return characters[id]
        }

        override suspend fun getFavoriteCharacterIds(): List<Int> {
            return characters.values.filter { it.isFavorite }.map { it.id }
        }

        override suspend fun updateFavorite(id: Int, isFavorite: Boolean): Int {
            val character = characters[id] ?: return 0
            characters[id] = character.copy(isFavorite = isFavorite)
            return 1
        }

        override suspend fun upsertCharacters(characters: List<CharacterEntity>) {
            characters.forEach { character -> this.characters[character.id] = character }
        }

        override suspend fun upsertCharacter(character: CharacterEntity) {
            characters[character.id] = character
        }

        override suspend fun clearCharacters() {
            characters.clear()
        }
    }
}
