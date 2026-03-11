package com.example.dattebayoapp.feature.characters.viewmodel

import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.repository.CharacterRepository
import com.example.dattebayoapp.domain.usecase.GetCharactersUseCase
import com.example.dattebayoapp.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCharacters updates ui state with loaded characters`() = runTest {
        val repository = FakeCharacterRepository()
        val viewModel = CharacterViewModel(
            getCharactersUseCase = GetCharactersUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(2, state.characters.size)
        assertEquals("Naruto Uzumaki", state.characters.first().name)
    }

    @Test
    fun `loadCharacters exposes error state on failure`() = runTest {
        val repository = FakeCharacterRepository(
            getCharactersError = IllegalStateException("Network unavailable"),
        )
        val viewModel = CharacterViewModel(
            getCharactersUseCase = GetCharactersUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network unavailable", state.errorMessage)
        assertTrue(state.characters.isEmpty())
    }

    @Test
    fun `toggleFavorite updates character favorite state in ui`() = runTest {
        val repository = FakeCharacterRepository()
        val viewModel = CharacterViewModel(
            getCharactersUseCase = GetCharactersUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.toggleFavorite(1344)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.characters.first { it.id == 1344 }.isFavorite)
        assertNull(state.errorMessage)
    }

    @Test
    fun `clearError removes current error message`() = runTest {
        val repository = FakeCharacterRepository(
            getCharactersError = IllegalStateException("Network unavailable"),
        )
        val viewModel = CharacterViewModel(
            getCharactersUseCase = GetCharactersUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    private class FakeCharacterRepository(
        private val getCharactersError: Throwable? = null,
    ) : CharacterRepository {
        private val favorites = mutableMapOf<Int, Boolean>()
        private val characters = listOf(
            CharacterListItem(
                id = 1344,
                name = "Naruto Uzumaki",
                images = listOf("image-1"),
                debut = CharacterDebut(manga = "Naruto Chapter #1"),
            ),
            CharacterListItem(
                id = 1307,
                name = "Sasuke Uchiha",
                images = listOf("image-2"),
                debut = CharacterDebut(manga = "Naruto Chapter #3"),
            ),
        )

        override suspend fun getCharacters(): CharacterPage {
            getCharactersError?.let { throw it }
            return CharacterPage(
                items = characters.map { character ->
                    character.copy(isFavorite = favorites[character.id] ?: false)
                },
                currentPage = 1,
                pageSize = 20,
                total = characters.size,
            )
        }

        override suspend fun getCharacterDetails(id: Int) = throw UnsupportedOperationException()

        override suspend fun getFavoriteCharacters(): List<CharacterListItem> {
            return characters
                .filter { character -> favorites[character.id] == true }
                .map { character -> character.copy(isFavorite = true) }
        }

        override suspend fun saveFavorite(id: Int): Boolean {
            favorites[id] = true
            return true
        }

        override suspend fun removeFavorite(id: Int): Boolean {
            val wasFavorite = favorites[id] == true
            favorites[id] = false
            return wasFavorite
        }

        override suspend fun toggleFavorite(id: Int): Boolean {
            val newState = !(favorites[id] ?: false)
            favorites[id] = newState
            return newState
        }
    }
}
