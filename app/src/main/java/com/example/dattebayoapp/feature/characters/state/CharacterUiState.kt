package com.example.dattebayoapp.feature.characters.state

import com.example.dattebayoapp.domain.model.CharacterListItem

data class CharacterUiState(
    val characters: List<CharacterListItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
