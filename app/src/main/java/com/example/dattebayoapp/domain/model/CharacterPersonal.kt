package com.example.dattebayoapp.domain.model

data class CharacterPersonal(
    val birthdate: String? = null,
    val sex: String? = null,
    val age: Map<String, String> = emptyMap(),
    val height: Map<String, String> = emptyMap(),
    val weight: Map<String, String> = emptyMap(),
    val bloodType: String? = null,
    val status: String? = null,
    val species: String? = null,
    val tailedBeast: String? = null,
    val kekkeiGenkai: List<String> = emptyList(),
    val classification: List<String> = emptyList(),
    val occupation: List<String> = emptyList(),
    val affiliation: List<String> = emptyList(),
    val team: List<String> = emptyList(),
    val clan: List<String> = emptyList(),
    val titles: List<String> = emptyList(),
    val partner: List<String> = emptyList(),
)
