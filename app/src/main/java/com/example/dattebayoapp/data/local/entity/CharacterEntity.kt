package com.example.dattebayoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterPersonal
import com.example.dattebayoapp.domain.model.CharacterRank
import com.example.dattebayoapp.domain.model.CharacterVoiceActors

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val images: List<String> = emptyList(),
    val debut: CharacterDebut = CharacterDebut(),
    val isFavorite: Boolean = false,
    val family: Map<String, String> = emptyMap(),
    val jutsu: List<String> = emptyList(),
    val natureType: List<String> = emptyList(),
    val personal: CharacterPersonal = CharacterPersonal(),
    val rank: CharacterRank = CharacterRank(),
    val tools: List<String> = emptyList(),
    val uniqueTraits: List<String> = emptyList(),
    val voiceActors: CharacterVoiceActors = CharacterVoiceActors(),
)
