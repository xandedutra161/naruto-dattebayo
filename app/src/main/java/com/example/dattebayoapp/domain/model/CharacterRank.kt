package com.example.dattebayoapp.domain.model

data class CharacterRank(
    val ninjaRank: Map<String, String> = emptyMap(),
    val ninjaRegistration: String? = null,
)
