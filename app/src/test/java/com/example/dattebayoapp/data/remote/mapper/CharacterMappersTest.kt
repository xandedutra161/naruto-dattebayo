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
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterMappersTest {

    @Test
    fun `characters response maps into character page`() {
        val dto = CharactersResponseDto(
            characters = listOf(
                CharacterSummaryDto(
                    id = 1344,
                    name = "Naruto Uzumaki",
                    images = listOf("image-1", "image-2"),
                    debut = CharacterDebutDto(manga = "Naruto Chapter #1"),
                ),
            ),
            currentPage = 1,
            pageSize = 20,
            total = 1431,
        )

        val result = dto.toDomain()

        assertEquals(
            CharacterPage(
                items = listOf(
                    CharacterListItem(
                        id = 1344,
                        name = "Naruto Uzumaki",
                        images = listOf("image-1", "image-2"),
                        debut = CharacterDebut(manga = "Naruto Chapter #1"),
                    ),
                ),
                currentPage = 1,
                pageSize = 20,
                total = 1431,
            ),
            result,
        )
    }

    @Test
    fun `character details maps dynamic maps and normalizes json elements`() {
        val dto = CharacterDetailsDto(
            id = 1344,
            name = "Naruto Uzumaki",
            images = listOf("image-1", "image-2"),
            debut = CharacterDebutDto(anime = "Naruto Episode #1"),
            family = mapOf("father" to "Minato Namikaze"),
            jutsu = listOf("Rasengan"),
            natureType = listOf("Wind Release"),
            personal = CharacterPersonalDto(
                age = mapOf("Part I" to "12-13"),
                height = mapOf("Part II" to "166cm"),
                weight = mapOf("Part II" to "50.9kg"),
                classification = jsonArray("Jinchuriki", "Sage"),
                team = jsonString("Team Kakashi"),
                clan = jsonString("Uzumaki"),
                affiliation = jsonArray("Konohagakure", "Mount Myoboku"),
            ),
            rank = CharacterRankDto(
                ninjaRank = mapOf("Part I" to "Genin"),
                ninjaRegistration = "012607",
            ),
            tools = listOf("Flying Thunder God Kunai"),
            uniqueTraits = listOf("Can absorb chakra"),
            voiceActors = CharacterVoiceActorsDto(
                japanese = jsonArray("Junko Takeuchi"),
                english = jsonString("Maile Flanagan"),
            ),
        )

        val result = dto.toDomain()

        assertEquals(
            CharacterDetails(
                id = 1344,
                name = "Naruto Uzumaki",
                images = listOf("image-1", "image-2"),
                debut = CharacterDebut(anime = "Naruto Episode #1"),
                family = mapOf("father" to "Minato Namikaze"),
                jutsu = listOf("Rasengan"),
                natureType = listOf("Wind Release"),
                personal = CharacterPersonal(
                    age = mapOf("Part I" to "12-13"),
                    height = mapOf("Part II" to "166cm"),
                    weight = mapOf("Part II" to "50.9kg"),
                    classification = listOf("Jinchuriki", "Sage"),
                    team = listOf("Team Kakashi"),
                    clan = listOf("Uzumaki"),
                    affiliation = listOf("Konohagakure", "Mount Myoboku"),
                ),
                rank = CharacterRank(
                    ninjaRank = mapOf("Part I" to "Genin"),
                    ninjaRegistration = "012607",
                ),
                tools = listOf("Flying Thunder God Kunai"),
                uniqueTraits = listOf("Can absorb chakra"),
                voiceActors = CharacterVoiceActors(
                    japanese = listOf("Junko Takeuchi"),
                    english = listOf("Maile Flanagan"),
                ),
            ),
            result,
        )
    }

    @Test
    fun `character details maps null and irregular json fields to empty lists`() {
        val dto = CharacterDetailsDto(
            id = 813,
            name = "Mitsuki",
            personal = CharacterPersonalDto(
                classification = jsonString("Medical-nin"),
                team = jsonString("Team 7"),
                partner = jsonObject("""{"name":"Orochimaru"}"""),
                titles = null,
            ),
            voiceActors = CharacterVoiceActorsDto(
                japanese = jsonString("Ryuichi Kijima"),
                english = jsonObject("""{"main":"Robbie Daymond"}"""),
            ),
        )

        val result = dto.toDomain()

        assertEquals(listOf("Medical-nin"), result.personal.classification)
        assertEquals(listOf("Team 7"), result.personal.team)
        assertTrue(result.personal.partner.isEmpty())
        assertTrue(result.personal.titles.isEmpty())
        assertEquals(listOf("Ryuichi Kijima"), result.voiceActors.japanese)
        assertTrue(result.voiceActors.english.isEmpty())
        assertTrue(result.family.isEmpty())
        assertTrue(result.rank.ninjaRank.isEmpty())
    }

    @Test
    fun `domain models do not expose gson json element`() {
        val domainClasses = listOf(
            CharacterPage::class.java,
            CharacterListItem::class.java,
            CharacterDetails::class.java,
            CharacterDebut::class.java,
            CharacterPersonal::class.java,
            CharacterRank::class.java,
            CharacterVoiceActors::class.java,
        )

        domainClasses.forEach { clazz ->
            clazz.declaredFields
                .filterNot { it.isSynthetic }
                .forEach { field ->
                    assertFalse(
                        "Field ${clazz.simpleName}.${field.name} exposes JsonElement",
                        JsonElement::class.java.isAssignableFrom(field.type),
                    )
                }
        }
    }

    private fun jsonString(value: String) = JsonParser.parseString("\"$value\"")

    private fun jsonArray(vararg values: String): JsonElement {
        return JsonParser.parseString(values.joinToString(prefix = "[", postfix = "]") { "\"$it\"" })
    }

    private fun jsonObject(value: String) = JsonParser.parseString(value)
}
