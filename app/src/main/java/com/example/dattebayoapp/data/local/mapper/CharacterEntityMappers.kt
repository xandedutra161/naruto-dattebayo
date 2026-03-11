package com.example.dattebayoapp.data.local.mapper

import com.example.dattebayoapp.data.local.entity.CharacterEntity
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.model.CharacterPersonal
import com.example.dattebayoapp.domain.model.CharacterRank
import com.example.dattebayoapp.domain.model.CharacterVoiceActors

fun CharacterListItem.toEntity(existing: CharacterEntity? = null): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        images = images,
        debut = debut ?: existing?.debut ?: CharacterDebut(),
        isFavorite = existing?.isFavorite ?: isFavorite,
        family = existing?.family.orEmpty(),
        jutsu = existing?.jutsu.orEmpty(),
        natureType = existing?.natureType.orEmpty(),
        personal = existing?.personal ?: CharacterPersonal(),
        rank = existing?.rank ?: CharacterRank(),
        tools = existing?.tools.orEmpty(),
        uniqueTraits = existing?.uniqueTraits.orEmpty(),
        voiceActors = existing?.voiceActors ?: CharacterVoiceActors(),
    )
}

fun CharacterDetails.toEntity(existing: CharacterEntity? = null): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        images = images,
        debut = debut,
        isFavorite = if (isFavorite) true else existing?.isFavorite ?: false,
        family = family,
        jutsu = jutsu,
        natureType = natureType,
        personal = personal,
        rank = rank,
        tools = tools,
        uniqueTraits = uniqueTraits,
        voiceActors = voiceActors,
    )
}

fun CharacterEntity.toListItem(): CharacterListItem {
    return CharacterListItem(
        id = id,
        name = name,
        images = images,
        debut = debut,
        isFavorite = isFavorite,
    )
}
