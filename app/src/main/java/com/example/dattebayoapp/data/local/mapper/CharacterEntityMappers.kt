package com.example.dattebayoapp.data.local.mapper

import com.example.dattebayoapp.data.local.entity.CharacterEntity
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem

fun CharacterListItem.toEntity(existing: CharacterEntity? = null): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        images = images,
        debut = debut ?: existing?.debut ?: com.example.dattebayoapp.domain.model.CharacterDebut(),
        isFavorite = existing?.isFavorite ?: isFavorite,
        family = existing?.family.orEmpty(),
        jutsu = existing?.jutsu.orEmpty(),
        natureType = existing?.natureType.orEmpty(),
        personal = existing?.personal ?: com.example.dattebayoapp.domain.model.CharacterPersonal(),
        rank = existing?.rank ?: com.example.dattebayoapp.domain.model.CharacterRank(),
        tools = existing?.tools.orEmpty(),
        uniqueTraits = existing?.uniqueTraits.orEmpty(),
        voiceActors = existing?.voiceActors ?: com.example.dattebayoapp.domain.model.CharacterVoiceActors(),
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
