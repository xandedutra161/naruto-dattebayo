package com.example.dattebayoapp.data.remote.dto

data class CharactersResponseDto(
    val characters: List<CharacterSummaryDto> = emptyList(),
    val currentPage: Int,
    val pageSize: Int,
    val total: Int,
)
