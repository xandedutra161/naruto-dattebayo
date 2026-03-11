package com.example.dattebayoapp.data.remote.dto

import com.google.gson.JsonElement

data class CharacterPersonalDto(
    val birthdate: String? = null,
    val sex: String? = null,
    val age: Map<String, String>? = null,
    val height: Map<String, String>? = null,
    val weight: Map<String, String>? = null,
    val bloodType: String? = null,
    val status: String? = null,
    val species: String? = null,
    val tailedBeast: String? = null,
    val kekkeiGenkai: JsonElement? = null,
    val classification: JsonElement? = null,
    val occupation: JsonElement? = null,
    val affiliation: JsonElement? = null,
    val team: JsonElement? = null,
    val clan: JsonElement? = null,
    val titles: JsonElement? = null,
    val partner: JsonElement? = null,
)
