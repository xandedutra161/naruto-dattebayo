package com.example.dattebayoapp.feature.characters.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dattebayoapp.domain.usecase.GetCharacterDetailUseCase
import com.example.dattebayoapp.domain.usecase.ToggleFavoriteUseCase
import com.example.dattebayoapp.feature.characters.state.CharacterDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    private val _uiState = MutableStateFlow(CharacterDetailUiState(isLoading = true))
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true, errorMessage = null) }

            runCatching { getCharacterDetailUseCase(characterId) }
                .onSuccess { character ->
                    _uiState.value = CharacterDetailUiState(
                        character = character,
                        isLoading = false,
                        errorMessage = null,
                    )
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "Não foi possível carregar os detalhes do personagem.",
                        )
                    }
                }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            runCatching { toggleFavoriteUseCase(characterId) }
                .onSuccess { isFavorite ->
                    _uiState.update { state ->
                        state.copy(
                            character = state.character?.copy(isFavorite = isFavorite),
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

    fun clearError() {
        _uiState.update { state -> state.copy(errorMessage = null) }
    }
}
