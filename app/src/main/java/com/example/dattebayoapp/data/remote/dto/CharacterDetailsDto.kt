package com.example.dattebayoapp.data.remote.dto

data class CharacterDetailsDto(
    val id: Int,
    val name: String,
    val images: List<String>? = null,
    val debut: CharacterDebutDto? = null,
    val family: Map<String, String>? = null,
    val jutsu: List<String>? = null,
    val natureType: List<String>? = null,
    val personal: CharacterPersonalDto? = null,
    val rank: CharacterRankDto? = null,
    val tools: List<String>? = null,
    val uniqueTraits: List<String>? = null,
    val voiceActors: CharacterVoiceActorsDto? = null,
)
