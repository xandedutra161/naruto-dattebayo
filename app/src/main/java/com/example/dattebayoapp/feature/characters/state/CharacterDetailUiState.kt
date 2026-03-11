package com.example.dattebayoapp.feature.characters.state

import com.example.dattebayoapp.domain.model.CharacterDetails

data class CharacterDetailUiState(
    val character: CharacterDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
