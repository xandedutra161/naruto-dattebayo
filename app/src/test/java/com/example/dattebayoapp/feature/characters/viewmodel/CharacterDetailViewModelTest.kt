package com.example.dattebayoapp.feature.characters.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.repository.CharacterRepository
import com.example.dattebayoapp.domain.usecase.GetCharacterDetailUseCase
import com.example.dattebayoapp.domain.usecase.ObserveFavoriteStatusUseCase
import com.example.dattebayoapp.domain.usecase.ToggleFavoriteUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {

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
    fun `detail uses observed favorite state after loading`() = runTest {
        val repository = FakeCharacterRepository(initialFavorite = true)
        val viewModel = CharacterDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("characterId" to 1344)),
            getCharacterDetailUseCase = GetCharacterDetailUseCase(repository),
            observeFavoriteStatusUseCase = ObserveFavoriteStatusUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.character?.isFavorite == true)
        assertNull(state.errorMessage)
    }

    @Test
    fun `toggleFavorite updates detail state from observed flow`() = runTest {
        val repository = FakeCharacterRepository(initialFavorite = true)
        val viewModel = CharacterDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("characterId" to 1344)),
            getCharacterDetailUseCase = GetCharacterDetailUseCase(repository),
            observeFavoriteStatusUseCase = ObserveFavoriteStatusUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
        )

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.toggleFavorite()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.character?.isFavorite == true)
        assertTrue(repository.lastToggledFavoriteId == 1344)
    }

    private class FakeCharacterRepository(
        initialFavorite: Boolean,
    ) : CharacterRepository {
        private val favoriteStatusFlow = MutableStateFlow(initialFavorite)
        var lastToggledFavoriteId: Int? = null

        override suspend fun getCharacters(): CharacterPage = throw UnsupportedOperationException()

        override suspend fun getCharacterDetails(id: Int): CharacterDetails {
            return CharacterDetails(
                id = id,
                name = "Naruto Uzumaki",
                images = listOf("image-1"),
                debut = CharacterDebut(manga = "Naruto Chapter #1"),
                isFavorite = false,
            )
        }

        override suspend fun getFavoriteCharacters(): List<CharacterListItem> {
            throw UnsupportedOperationException()
        }

        override fun observeFavoriteCharacters(): Flow<List<CharacterListItem>> {
            return favoriteStatusFlow.map { isFavorite ->
                if (isFavorite) {
                    listOf(
                        CharacterListItem(
                            id = 1344,
                            name = "Naruto Uzumaki",
                            isFavorite = true,
                        ),
                    )
                } else {
                    emptyList()
                }
            }
        }

        override fun observeFavoriteCharacterIds(): Flow<Set<Int>> {
            return favoriteStatusFlow.map { isFavorite ->
                if (isFavorite) setOf(1344) else emptySet()
            }
        }

        override fun observeFavoriteStatus(id: Int): Flow<Boolean> = favoriteStatusFlow

        override suspend fun saveFavorite(id: Int): Boolean = true

        override suspend fun removeFavorite(id: Int): Boolean = true

        override suspend fun toggleFavorite(id: Int): Boolean {
            lastToggledFavoriteId = id
            val nextValue = !favoriteStatusFlow.value
            favoriteStatusFlow.value = nextValue
            return nextValue
        }
    }
}
