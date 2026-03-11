package com.example.dattebayoapp.navigation

sealed class AppDestination(
    val route: String,
) {
    data object Characters : AppDestination("characters")

    data object Favorites : AppDestination("favorites")

    data object CharacterDetail : AppDestination("character_detail/{characterId}") {
        fun createRoute(characterId: Int): String = "character_detail/$characterId"
    }
}
