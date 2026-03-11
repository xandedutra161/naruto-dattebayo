package com.example.dattebayoapp.domain.model

data class CharacterDetails(
    val id: Int,
    val name: String,
    val images: List<String> = emptyList(),
    val debut: CharacterDebut = CharacterDebut(),
    val family: Map<String, String> = emptyMap(),
    val jutsu: List<String> = emptyList(),
    val natureType: List<String> = emptyList(),
    val personal: CharacterPersonal = CharacterPersonal(),
    val rank: CharacterRank = CharacterRank(),
    val tools: List<String> = emptyList(),
    val uniqueTraits: List<String> = emptyList(),
    val voiceActors: CharacterVoiceActors = CharacterVoiceActors(),
)
