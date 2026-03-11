package com.example.dattebayoapp.data.remote.mapper

import com.example.dattebayoapp.data.remote.dto.CharacterDebutDto
import com.example.dattebayoapp.data.remote.dto.CharacterDetailsDto
import com.example.dattebayoapp.data.remote.dto.CharacterPersonalDto
import com.example.dattebayoapp.data.remote.dto.CharacterRankDto
import com.example.dattebayoapp.data.remote.dto.CharacterSummaryDto
import com.example.dattebayoapp.data.remote.dto.CharacterVoiceActorsDto
import com.example.dattebayoapp.data.remote.dto.CharactersResponseDto
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.model.CharacterPersonal
import com.example.dattebayoapp.domain.model.CharacterRank
import com.example.dattebayoapp.domain.model.CharacterVoiceActors
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

fun CharactersResponseDto.toDomain(): CharacterPage {
    return CharacterPage(
        items = characters.map { it.toDomain() },
        currentPage = currentPage,
        pageSize = pageSize,
        total = total,
    )
}

fun CharacterSummaryDto.toDomain(): CharacterListItem {
    return CharacterListItem(
        id = id,
        name = name,
        images = images,
        debut = debut?.toDomain(),
        isFavorite = false,
    )
}

fun CharacterDetailsDto.toDomain(): CharacterDetails {
    return CharacterDetails(
        id = id,
        name = name,
        images = images.orEmpty(),
        debut = debut?.toDomain() ?: CharacterDebut(),
        isFavorite = false,
        family = family.orEmpty(),
        jutsu = jutsu.orEmpty(),
        natureType = natureType.orEmpty(),
        personal = personal?.toDomain() ?: CharacterPersonal(),
        rank = rank?.toDomain() ?: CharacterRank(),
        tools = tools.orEmpty(),
        uniqueTraits = uniqueTraits.orEmpty(),
        voiceActors = voiceActors?.toDomain() ?: CharacterVoiceActors(),
    )
}

private fun CharacterDebutDto.toDomain(): CharacterDebut {
    return CharacterDebut(
        manga = manga,
        anime = anime,
        novel = novel,
        movie = movie,
        game = game,
        ova = ova,
        appearsIn = appearsIn,
    )
}

private fun CharacterPersonalDto.toDomain(): CharacterPersonal {
    return CharacterPersonal(
        birthdate = birthdate,
        sex = sex,
        age = age.orEmpty(),
        height = height.orEmpty(),
        weight = weight.orEmpty(),
        bloodType = bloodType,
        status = status,
        species = species,
        tailedBeast = tailedBeast,
        kekkeiGenkai = kekkeiGenkai.toStringList(),
        classification = classification.toStringList(),
        occupation = occupation.toStringList(),
        affiliation = affiliation.toStringList(),
        team = team.toStringList(),
        clan = clan.toStringList(),
        titles = titles.toStringList(),
        partner = partner.toStringList(),
    )
}

private fun CharacterRankDto.toDomain(): CharacterRank {
    return CharacterRank(
        ninjaRank = ninjaRank.orEmpty(),
        ninjaRegistration = ninjaRegistration,
    )
}

private fun CharacterVoiceActorsDto.toDomain(): CharacterVoiceActors {
    return CharacterVoiceActors(
        japanese = japanese.toStringList(),
        english = english.toStringList(),
    )
}

private fun JsonElement?.toStringList(): List<String> {
    return when {
        this == null || isJsonNull -> emptyList()
        isJsonArray -> asJsonArray.mapNotNull { element ->
            val primitive = element as? JsonPrimitive
            if (primitive?.isString == true) primitive.asString else null
        }
        isJsonPrimitive && asJsonPrimitive.isString -> listOf(asString)
        else -> emptyList()
    }
}
