package com.example.dattebayoapp.feature.characters.state

import com.example.dattebayoapp.domain.model.CharacterListItem

data class FavoriteCharactersUiState(
    val favorites: List<CharacterListItem> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val filteredFavorites: List<CharacterListItem>
        get() = if (searchQuery.isBlank()) {
            favorites
        } else {
            favorites.filter { character ->
                character.name.contains(searchQuery, ignoreCase = true)
            }
        }
}
