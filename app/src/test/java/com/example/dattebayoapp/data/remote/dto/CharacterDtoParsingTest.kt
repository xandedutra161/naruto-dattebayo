package com.example.dattebayoapp.data.remote.dto

import com.example.dattebayoapp.core.network.RetrofitClient
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CharacterDtoParsingTest {

    private lateinit var gson: Gson

    @Before
    fun setup() {
        gson = RetrofitClient.createGson()
    }

    @Test
    fun `characters response parses pagination and summary data`() {
        val response = gson.fromJson(charactersListJson, CharactersResponseDto::class.java)

        assertEquals(1, response.currentPage)
        assertEquals(20, response.pageSize)
        assertEquals(1431, response.total)
        assertEquals(2, response.characters.size)
        assertEquals(1344, response.characters.first().id)
        assertEquals("Naruto Uzumaki", response.characters.first().name)
        assertEquals(2, response.characters.first().images.size)
        assertEquals("Naruto Chapter #1", response.characters.first().debut?.manga)
    }

    @Test
    fun `character details parses stable blocks and dynamic maps`() {
        val response = gson.fromJson(characterDetailsJson, CharacterDetailsDto::class.java)

        assertEquals(1344, response.id)
        assertEquals("Naruto Uzumaki", response.name)
        assertEquals(2, response.images.size)
        assertEquals("Minato Namikaze", response.family?.get("father"))
        assertEquals("12–13", response.personal?.age?.get("Part I"))
        assertEquals("Genin", response.rank?.ninjaRank?.get("Part I"))
        assertEquals(2, response.voiceActors?.japanese?.asJsonArray?.size())
        assertEquals(5, response.voiceActors?.english?.asJsonArray?.size())
        assertTrue(response.personal?.classification?.isJsonArray == true)
        assertTrue(response.personal?.team?.isJsonArray == true)
    }

    @Test
    fun `character details keeps inconsistent fields as json elements`() {
        val response = gson.fromJson(heterogeneousCharacterJson, CharacterDetailsDto::class.java)

        assertNotNull(response.personal)
        assertTrue(response.personal?.classification?.isJsonPrimitive == true)
        assertEquals("Medical-nin", response.personal?.classification?.asString)
        assertTrue(response.personal?.team?.isJsonPrimitive == true)
        assertEquals("Team 7", response.personal?.team?.asString)
        assertTrue(response.voiceActors?.japanese?.isJsonPrimitive == true)
        assertEquals("Ryūichi Kijima", response.voiceActors?.japanese?.asString)
        assertTrue(response.voiceActors?.english?.isJsonPrimitive == true)
        assertEquals("Robbie Daymond", response.voiceActors?.english?.asString)
    }

    private companion object {
        const val charactersListJson = """
            {
              "characters": [
                {
                  "id": 1344,
                  "name": "Naruto Uzumaki",
                  "images": [
                    "https://static.wikia.nocookie.net/naruto/images/d/d6/Naruto_Part_I.png",
                    "https://static.wikia.nocookie.net/naruto/images/7/7d/Naruto_Part_II.png"
                  ],
                  "debut": {
                    "manga": "Naruto Chapter #1",
                    "anime": "Naruto Episode #1",
                    "novel": "Naruto: Innocent Heart, Demonic Blood",
                    "movie": "Naruto the Movie: Ninja Clash in the Land of Snow",
                    "game": "Naruto: Konoha Ninpōchō",
                    "ova": "Find the Four-Leaf Red Clover!",
                    "appearsIn": "Anime, Manga, Novel, Game, Movie"
                  }
                },
                {
                  "id": 1307,
                  "name": "Sasuke Uchiha",
                  "images": [
                    "https://static.wikia.nocookie.net/naruto/images/2/21/Sasuke_Part_1.png",
                    "https://static.wikia.nocookie.net/naruto/images/1/13/Sasuke_Part_2.png"
                  ],
                  "debut": {
                    "manga": "Naruto Chapter #3",
                    "anime": "Naruto Episode #1",
                    "novel": "Naruto: Innocent Heart, Demonic Blood",
                    "movie": "Naruto the Movie: Ninja Clash in the Land of Snow",
                    "game": "Naruto: Konoha Ninpōchō",
                    "ova": "Find the Four-Leaf Red Clover!",
                    "appearsIn": "Anime, Manga, Novel, Game, Movie"
                  }
                }
              ],
              "currentPage": 1,
              "pageSize": 20,
              "total": 1431
            }
        """

        const val characterDetailsJson = """
            {
              "id": 1344,
              "name": "Naruto Uzumaki",
              "images": [
                "https://static.wikia.nocookie.net/naruto/images/d/d6/Naruto_Part_I.png",
                "https://static.wikia.nocookie.net/naruto/images/7/7d/Naruto_Part_II.png"
              ],
              "debut": {
                "manga": "Naruto Chapter #1",
                "anime": "Naruto Episode #1",
                "novel": "Naruto: Innocent Heart, Demonic Blood",
                "movie": "Naruto the Movie: Ninja Clash in the Land of Snow",
                "game": "Naruto: Konoha Ninpōchō",
                "ova": "Find the Four-Leaf Red Clover!",
                "appearsIn": "Anime, Manga, Novel, Game, Movie"
              },
              "family": {
                "father": "Minato Namikaze",
                "mother": "Kushina Uzumaki",
                "adoptive son": "Kawaki"
              },
              "jutsu": ["Rasengan", "Shadow Clone Technique"],
              "natureType": ["Wind Release  (Affinity)", "Lightning Release"],
              "personal": {
                "birthdate": "October 10",
                "sex": "Male",
                "age": {
                  "Part I": "12–13",
                  "Part II": "15–17",
                  "Academy Graduate": "12"
                },
                "height": {
                  "Part I": "145.3cm - 147.5cm",
                  "Part II": "166cm"
                },
                "weight": {
                  "Part I": "40.1kg - 40.6kg",
                  "Part II": "50.9kg"
                },
                "bloodType": "B",
                "kekkeiGenkai": ["Lava Release", "Magnet Release", "Boil Release"],
                "classification": ["Jinchūriki", "Sage", "Sensor Type"],
                "tailedBeast": "Kurama (Forms)",
                "occupation": ["Hokage", "Chūnin Exams Proctor  (Anime only)"],
                "affiliation": ["Konohagakure", "Mount Myōboku"],
                "team": ["Team Kakashi", "Sasuke Recovery Team"],
                "clan": "Uzumaki",
                "titles": ["seventhHokage(七代目火影,NanadaimeHokage)"]
              },
              "rank": {
                "ninjaRank": {
                  "Part I": "Genin",
                  "Gaiden": "Kage"
                },
                "ninjaRegistration": "012607"
              },
              "tools": ["Absorbing Hand", "Flying Thunder God Kunai"],
              "voiceActors": {
                "japanese": [
                  "Junko Takeuchi",
                  "Ema Kogure   (Sexy Technique)"
                ],
                "english": [
                  "Maile Flanagan",
                  "Stephanie Sheh   (Sexy Technique)",
                  "Jeannie Elias   (Sexy Technique) (episode 53-55)",
                  "Mary Elizabeth McGlynn   (Sexy Technique) (episode 177)",
                  "Kate Higgins   (Sexy Technique) (episode 229)"
                ]
              }
            }
        """

        const val heterogeneousCharacterJson = """
            {
              "id": 813,
              "name": "Mitsuki",
              "images": [
                "https://static.wikia.nocookie.net/naruto/images/5/5c/Mitsuki.png"
              ],
              "debut": {
                "manga": "Naruto Chapter #700+1",
                "anime": "Boruto Episode #4",
                "novel": "Boruto: Naruto the Movie",
                "movie": "Boruto: Naruto the Movie",
                "game": "Naruto Shippūden: Ultimate Ninja Storm 4",
                "ova": "Boruto OVA",
                "appearsIn": "Anime, Manga, Novel, Game, Movie"
              },
              "family": {
                "genetic template/parent": "Orochimaru",
                "clone/brother": "Log",
                "pet ": "Mikazuki"
              },
              "jutsu": ["Sage Mode", "Snake Clone Technique"],
              "natureType": ["Wind Release", "Lightning Release"],
              "personal": {
                "birthdate": "July 25",
                "sex": "Male",
                "species": "Synthetic Human",
                "height": {
                  "Gaiden": "149cm"
                },
                "kekkeiGenkai": "Jūgo's Clan's Kekkei Genkai",
                "classification": "Medical-nin",
                "affiliation": ["Otogakure", "Ryūchi Cave", "Konohagakure"],
                "team": "Team 7",
                "clan": null,
                "partner": null
              },
              "rank": {
                "ninjaRank": {
                  "Gaiden": "Genin"
                }
              },
              "tools": ["Poison", "Sword"],
              "voiceActors": {
                "japanese": "Ryūichi Kijima",
                "english": "Robbie Daymond"
              }
            }
        """
    }
}
