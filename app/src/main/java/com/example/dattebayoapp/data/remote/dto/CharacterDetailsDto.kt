package com.example.dattebayoapp.data.remote.dto

data class CharacterDetailsDto(
    val id: Int,
    val name: String,
    val images: List<String> = emptyList(),
    val debut: CharacterDebutDto? = null,
    val family: Map<String, String>? = null,
    val jutsu: List<String> = emptyList(),
    val natureType: List<String> = emptyList(),
    val personal: CharacterPersonalDto? = null,
    val rank: CharacterRankDto? = null,
    val tools: List<String> = emptyList(),
    val uniqueTraits: List<String> = emptyList(),
    val voiceActors: CharacterVoiceActorsDto? = null,
)
