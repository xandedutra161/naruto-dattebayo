package com.example.dattebayoapp.feature.characters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dattebayoapp.domain.usecase.ObserveFavoriteCharactersUseCase
import com.example.dattebayoapp.domain.usecase.RemoveFavoriteUseCase
import com.example.dattebayoapp.feature.characters.state.FavoriteCharactersUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteCharactersViewModel(
    private val observeFavoriteCharactersUseCase: ObserveFavoriteCharactersUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteCharactersUiState(isLoading = true))
    val uiState: StateFlow<FavoriteCharactersUiState> = _uiState.asStateFlow()
    private var observeFavoritesJob: Job? = null

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        if (observeFavoritesJob != null) {
            _uiState.update { state -> state.copy(errorMessage = null) }
            return
        }

        observeFavoritesJob = viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                observeFavoriteCharactersUseCase().collect { favorites ->
                    _uiState.update { state ->
                        state.copy(
                            favorites = favorites,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
            }.onFailure { throwable ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível carregar os favoritos.",
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state -> state.copy(searchQuery = query) }
    }

    fun removeFavorite(characterId: Int) {
        viewModelScope.launch {
            runCatching { removeFavoriteUseCase(characterId) }
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = "Não foi possível remover o favorito.",
                        )
                    }
                }
        }
    }
}
