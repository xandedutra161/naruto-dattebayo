package com.example.dattebayoapp.data.remote.dto

data class CharacterSummaryDto(
    val id: Int,
    val name: String,
    val images: List<String> = emptyList(),
    val debut: CharacterDebutDto? = null,
)
