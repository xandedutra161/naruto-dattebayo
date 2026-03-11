package com.example.dattebayoapp.data.repository

import com.example.dattebayoapp.data.local.dao.CharacterDao
import com.example.dattebayoapp.data.local.entity.CharacterEntity
import com.example.dattebayoapp.data.remote.service.NarutoApiService
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CharacterRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var narutoApiService: NarutoApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        narutoApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(NarutoApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getCharacters returns mapped domain page`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(charactersResponseJson),
        )
        val repository = CharacterRepositoryImpl(
            narutoApiService = narutoApiService,
            characterDao = FakeCharacterDao(),
        )

        val result = repository.getCharacters()
        val request = mockWebServer.takeRequest()

        assertEquals("/characters", request.path)
        assertEquals(1, result.currentPage)
        assertEquals(20, result.pageSize)
        assertEquals(1431, result.total)
        assertEquals(1, result.items.size)
        assertEquals("Naruto Uzumaki", result.items.first().name)
        assertEquals("Naruto Chapter #1", result.items.first().debut?.manga)
    }

    @Test
    fun `getCharacterDetails returns mapped domain details`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(characterDetailResponseJson),
        )
        val repository = CharacterRepositoryImpl(
            narutoApiService = narutoApiService,
            characterDao = FakeCharacterDao(),
        )

        val result = repository.getCharacterDetails(id = 1344)
        val request = mockWebServer.takeRequest()

        assertEquals("/characters/1344", request.path)
        assertEquals(1344, result.id)
        assertEquals("Naruto Uzumaki", result.name)
        assertEquals("Minato Namikaze", result.family["father"])
        assertEquals(listOf("Jinchuriki"), result.personal.classification)
        assertEquals(mapOf("Part I" to "Genin"), result.rank.ninjaRank)
        assertEquals(false, result.isFavorite)
    }

    @Test
    fun `saveFavorite caches remote detail when character is not local yet`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(characterDetailResponseJson),
        )
        val characterDao = FakeCharacterDao()
        val repository = CharacterRepositoryImpl(
            narutoApiService = narutoApiService,
            characterDao = characterDao,
        )

        val result = repository.saveFavorite(1344)
        val request = mockWebServer.takeRequest()

        assertEquals("/characters/1344", request.path)
        assertEquals(true, result)
        assertEquals(true, characterDao.getCharacterById(1344)?.isFavorite)
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
            narutoApiService = narutoApiService,
            characterDao = characterDao,
        )

        val result = repository.toggleFavorite(1344)

        assertEquals(true, result)
        assertEquals(true, characterDao.getCharacterById(1344)?.isFavorite)
    }

    @Test
    fun `removeFavorite updates cached character state`() = runTest {
        val characterDao = FakeCharacterDao(
            initialCharacters = mutableMapOf(
                1344 to CharacterEntity(
                    id = 1344,
                    name = "Naruto Uzumaki",
                    isFavorite = true,
                ),
            ),
        )
        val repository = CharacterRepositoryImpl(
            narutoApiService = narutoApiService,
            characterDao = characterDao,
        )

        val result = repository.removeFavorite(1344)

        assertTrue(result)
        assertEquals(false, characterDao.getCharacterById(1344)?.isFavorite)
    }

    @Test
    fun `getFavoriteCharacters returns mapped favorite items`() = runTest {
        val characterDao = FakeCharacterDao(
            initialCharacters = mutableMapOf(
                1344 to CharacterEntity(
                    id = 1344,
                    name = "Naruto Uzumaki",
                    images = listOf("image-1"),
                    debut = CharacterDebut(manga = "Naruto Chapter #1"),
                    isFavorite = true,
                ),
                1307 to CharacterEntity(
                    id = 1307,
                    name = "Sasuke Uchiha",
                    isFavorite = false,
                ),
            ),
        )
        val repository = CharacterRepositoryImpl(
            narutoApiService = narutoApiService,
            characterDao = characterDao,
        )

        val result = repository.getFavoriteCharacters()

        assertEquals(1, result.size)
        assertEquals(1344, result.first().id)
        assertEquals(true, result.first().isFavorite)
        assertEquals("Naruto Chapter #1", result.first().debut?.manga)
    }

    @Test
    fun `observeFavoriteCharacterIds exposes current favorite ids`() = runTest {
        val repository = CharacterRepositoryImpl(
            narutoApiService = narutoApiService,
            characterDao = FakeCharacterDao(
                initialCharacters = mutableMapOf(
                    1344 to CharacterEntity(id = 1344, name = "Naruto Uzumaki", isFavorite = true),
                    1307 to CharacterEntity(id = 1307, name = "Sasuke Uchiha", isFavorite = false),
                ),
            ),
        )

        val result = repository.observeFavoriteCharacterIds().first()

        assertEquals(setOf(1344), result)
    }

    @Test
    fun `observeFavoriteStatus reflects dao updates`() = runTest {
        val characterDao = FakeCharacterDao(
            initialCharacters = mutableMapOf(
                1344 to CharacterEntity(id = 1344, name = "Naruto Uzumaki", isFavorite = false),
            ),
        )
        val repository = CharacterRepositoryImpl(
            narutoApiService = narutoApiService,
            characterDao = characterDao,
        )

        characterDao.updateFavorite(1344, true)
        val result = repository.observeFavoriteStatus(1344).first()

        assertTrue(result)
    }

    private class FakeCharacterDao(
        initialCharacters: MutableMap<Int, CharacterEntity> = mutableMapOf(),
    ) : CharacterDao {
        private val characters = initialCharacters
        private val charactersFlow = MutableStateFlow(characters.toMap())

        override suspend fun getCharacters(): List<CharacterEntity> {
            return characters.values.sortedBy { it.id }
        }

        override suspend fun getCharacterById(id: Int): CharacterEntity? {
            return characters[id]
        }

        override suspend fun getFavoriteCharacters(): List<CharacterEntity> {
            return characters.values.filter { it.isFavorite }.sortedBy { it.name }
        }

        override fun observeFavoriteCharacters(): Flow<List<CharacterEntity>> {
            return charactersFlow.map { allCharacters ->
                allCharacters.values.filter { it.isFavorite }.sortedBy { it.name }
            }
        }

        override suspend fun getFavoriteCharacterIds(): List<Int> {
            return characters.values.filter { it.isFavorite }.map { it.id }
        }

        override fun observeFavoriteCharacterIds(): Flow<List<Int>> {
            return charactersFlow.map { allCharacters ->
                allCharacters.values.filter { it.isFavorite }.map { it.id }
            }
        }

        override fun observeFavoriteStatus(id: Int): Flow<Boolean?> {
            return charactersFlow.map { allCharacters -> allCharacters[id]?.isFavorite }
        }

        override suspend fun updateFavorite(id: Int, isFavorite: Boolean): Int {
            val character = characters[id] ?: return 0
            characters[id] = character.copy(isFavorite = isFavorite)
            charactersFlow.value = characters.toMap()
            return 1
        }

        override suspend fun upsertCharacters(characters: List<CharacterEntity>) {
            characters.forEach { character -> this.characters[character.id] = character }
            charactersFlow.value = this.characters.toMap()
        }

        override suspend fun upsertCharacter(character: CharacterEntity) {
            characters[character.id] = character
            charactersFlow.value = characters.toMap()
        }
    }

    private companion object {
        const val charactersResponseJson = """
            {
              "characters": [
                {
                  "id": 1344,
                  "name": "Naruto Uzumaki",
                  "images": ["image-1"],
                  "debut": {
                    "manga": "Naruto Chapter #1",
                    "anime": "Naruto Episode #1",
                    "appearsIn": "Anime, Manga"
                  }
                }
              ],
              "currentPage": 1,
              "pageSize": 20,
              "total": 1431
            }
        """

        const val characterDetailResponseJson = """
            {
              "id": 1344,
              "name": "Naruto Uzumaki",
              "images": ["image-1", "image-2"],
              "family": {
                "father": "Minato Namikaze"
              },
              "jutsu": ["Rasengan"],
              "natureType": ["Wind Release"],
              "personal": {
                "classification": ["Jinchuriki"]
              },
              "rank": {
                "ninjaRank": {
                  "Part I": "Genin"
                },
                "ninjaRegistration": "012607"
              },
              "tools": ["Kunai"],
              "uniqueTraits": []
            }
        """
    }
}
