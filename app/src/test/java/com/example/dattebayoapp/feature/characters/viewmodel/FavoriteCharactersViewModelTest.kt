package com.example.dattebayoapp.feature.characters.viewmodel

import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.repository.CharacterRepository
import com.example.dattebayoapp.domain.usecase.ObserveFavoriteCharactersUseCase
import com.example.dattebayoapp.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteCharactersViewModelTest {

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
    fun `favorites screen reacts to repository updates`() = runTest {
        val repository = FakeCharacterRepository()
        val viewModel = FavoriteCharactersViewModel(
            observeFavoriteCharactersUseCase = ObserveFavoriteCharactersUseCase(repository),
            removeFavoriteUseCase = RemoveFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()
        repository.emitFavorites(
            listOf(
                CharacterListItem(id = 1344, name = "Naruto Uzumaki", isFavorite = true),
                CharacterListItem(id = 1307, name = "Sasuke Uchiha", isFavorite = true),
            ),
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.favorites.size)
    }

    @Test
    fun `search keeps working with reactive favorites`() = runTest {
        val repository = FakeCharacterRepository()
        val viewModel = FavoriteCharactersViewModel(
            observeFavoriteCharactersUseCase = ObserveFavoriteCharactersUseCase(repository),
            removeFavoriteUseCase = RemoveFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onSearchQueryChange("Sasuke")
        repository.emitFavorites(
            listOf(
                CharacterListItem(id = 1344, name = "Naruto Uzumaki", isFavorite = true),
                CharacterListItem(id = 1307, name = "Sasuke Uchiha", isFavorite = true),
            ),
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.filteredFavorites.size)
        assertEquals("Sasuke Uchiha", state.filteredFavorites.single().name)
    }

    @Test
    fun `removeFavorite relies on reactive source to update list`() = runTest {
        val repository = FakeCharacterRepository()
        val viewModel = FavoriteCharactersViewModel(
            observeFavoriteCharactersUseCase = ObserveFavoriteCharactersUseCase(repository),
            removeFavoriteUseCase = RemoveFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.removeFavorite(1344)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.favorites.isEmpty())
    }

    private class FakeCharacterRepository : CharacterRepository {
        private val favoritesFlow = MutableStateFlow(
            listOf(
                CharacterListItem(
                    id = 1344,
                    name = "Naruto Uzumaki",
                    debut = CharacterDebut(manga = "Naruto Chapter #1"),
                    isFavorite = true,
                ),
            ),
        )

        override suspend fun getCharacters(): CharacterPage = throw UnsupportedOperationException()

        override suspend fun getCharacterDetails(id: Int): CharacterDetails {
            throw UnsupportedOperationException()
        }

        override suspend fun getFavoriteCharacters(): List<CharacterListItem> = favoritesFlow.value

        override fun observeFavoriteCharacters(): Flow<List<CharacterListItem>> = favoritesFlow

        override fun observeFavoriteCharacterIds(): Flow<Set<Int>> {
            return favoritesFlow.map { favorites -> favorites.map { it.id }.toSet() }
        }

        override fun observeFavoriteStatus(id: Int): Flow<Boolean> {
            return favoritesFlow.map { favorites -> favorites.any { it.id == id } }
        }

        override suspend fun saveFavorite(id: Int): Boolean = true

        override suspend fun removeFavorite(id: Int): Boolean {
            favoritesFlow.value = favoritesFlow.value.filterNot { it.id == id }
            return true
        }

        override suspend fun toggleFavorite(id: Int): Boolean = true

        fun emitFavorites(favorites: List<CharacterListItem>) {
            favoritesFlow.value = favorites
        }
    }
}
