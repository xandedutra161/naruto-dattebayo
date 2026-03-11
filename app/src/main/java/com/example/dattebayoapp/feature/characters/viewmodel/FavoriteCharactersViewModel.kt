package com.example.dattebayoapp.feature.characters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dattebayoapp.domain.usecase.GetFavoriteCharactersUseCase
import com.example.dattebayoapp.domain.usecase.RemoveFavoriteUseCase
import com.example.dattebayoapp.feature.characters.state.FavoriteCharactersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteCharactersViewModel @Inject constructor(
    private val getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteCharactersUiState(isLoading = true))
    val uiState: StateFlow<FavoriteCharactersUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true, errorMessage = null)
            }

            runCatching { getFavoriteCharactersUseCase() }
                .onSuccess { favorites ->
                    _uiState.update { state ->
                        state.copy(
                            favorites = favorites,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load favorites.",
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
                            favorites = state.favorites.filterNot { it.id == characterId },
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = throwable.message ?: "Unable to remove favorite.",
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { state -> state.copy(errorMessage = null) }
    }
}
