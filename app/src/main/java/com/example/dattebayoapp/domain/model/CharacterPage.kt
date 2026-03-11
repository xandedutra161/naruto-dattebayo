package com.example.dattebayoapp.domain.model

data class CharacterPage(
    val items: List<CharacterListItem> = emptyList(),
    val currentPage: Int,
    val pageSize: Int,
    val total: Int,
)
