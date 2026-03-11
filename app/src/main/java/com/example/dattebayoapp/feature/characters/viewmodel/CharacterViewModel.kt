package com.example.dattebayoapp.feature.characters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dattebayoapp.domain.usecase.GetCharactersUseCase
import com.example.dattebayoapp.domain.usecase.ToggleFavoriteUseCase
import com.example.dattebayoapp.feature.characters.state.CharacterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterUiState(isLoading = true))
    val uiState: StateFlow<CharacterUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
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
                    _uiState.value = CharacterUiState(
                        characters = page.items,
                        isLoading = false,
                        errorMessage = null,
                    )
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load characters.",
                        )
                    }
                }
        }
    }

    fun toggleFavorite(characterId: Int) {
        viewModelScope.launch {
            runCatching { toggleFavoriteUseCase(characterId) }
                .onSuccess { isFavorite ->
                    _uiState.update { state ->
                        state.copy(
                            characters = state.characters.map { character ->
                                if (character.id == characterId) {
                                    character.copy(isFavorite = isFavorite)
                                } else {
                                    character
                                }
                            },
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = throwable.message ?: "Unable to update favorite state.",
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { state -> state.copy(errorMessage = null) }
    }
}
