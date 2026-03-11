package com.example.dattebayoapp.domain.model

data class CharacterListItem(
    val id: Int,
    val name: String,
    val images: List<String> = emptyList(),
    val debut: CharacterDebut? = null,
    val isFavorite: Boolean = false,
)
