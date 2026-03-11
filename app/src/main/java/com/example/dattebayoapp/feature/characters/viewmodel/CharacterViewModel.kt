package com.example.dattebayoapp.feature.characters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.usecase.GetCharactersUseCase
import com.example.dattebayoapp.domain.usecase.ObserveFavoriteCharacterIdsUseCase
import com.example.dattebayoapp.domain.usecase.RemoveFavoriteUseCase
import com.example.dattebayoapp.domain.usecase.SaveFavoriteUseCase
import com.example.dattebayoapp.feature.characters.state.CharacterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val observeFavoriteCharacterIdsUseCase: ObserveFavoriteCharacterIdsUseCase,
    private val saveFavoriteUseCase: SaveFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterUiState(isLoading = true))
    val uiState: StateFlow<CharacterUiState> = _uiState.asStateFlow()
    private val loadedCharacters = MutableStateFlow<List<CharacterListItem>>(emptyList())

    init {
        observeFavorites()
        loadCharacters()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            observeFavoriteCharacterIdsUseCase().collect { favoriteIds ->
                _uiState.update { state ->
                    state.copy(
                        characters = loadedCharacters.value.map { character ->
                            character.copy(isFavorite = character.id in favoriteIds)
                        },
                    )
                }
            }
        }
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            runCatching { getCharactersUseCase() }
                .onSuccess { page ->
                    loadedCharacters.value = page.items
                    _uiState.update { state ->
                        state.copy(
                            characters = page.items.map { character ->
                                character.copy(
                                    isFavorite = state.characters.firstOrNull { it.id == character.id }?.isFavorite
                                        ?: character.isFavorite,
                                )
                            },
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "Não foi possível carregar os personagens.",
                        )
                }
            }
        }
    }

    fun toggleFavorite(characterId: Int) {
        viewModelScope.launch {
            val isCurrentlyFavorite = uiState.value.characters
                .firstOrNull { character -> character.id == characterId }
                ?.isFavorite
                ?: return@launch

            runCatching {
                if (isCurrentlyFavorite) {
                    removeFavoriteUseCase(characterId)
                    false
                } else {
                    saveFavoriteUseCase(characterId)
                }
            }
                .onSuccess { isFavorite ->
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = "Não foi possível atualizar o favorito.",
                        )
                    }
                }
        }
    }
}
