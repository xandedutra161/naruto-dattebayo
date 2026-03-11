package com.example.dattebayoapp.feature.characters.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.feature.characters.state.FavoriteCharactersUiState
import com.example.dattebayoapp.ui.theme.DattebayoAppTheme
import org.junit.Rule
import org.junit.Test

class FavoriteCharactersScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun searchFiltersFavoriteCharacters() {
        val favorites = listOf(
            CharacterListItem(
                id = 1344,
                name = "Naruto Uzumaki",
                debut = CharacterDebut(manga = "Naruto Chapter #1"),
                isFavorite = true,
            ),
            CharacterListItem(
                id = 1307,
                name = "Sasuke Uchiha",
                debut = CharacterDebut(manga = "Naruto Chapter #3"),
                isFavorite = true,
            ),
        )

        composeRule.setContent {
            var uiState by mutableStateOf(
                FavoriteCharactersUiState(
                    favorites = favorites,
                    isLoading = false,
                ),
            )

            DattebayoAppTheme {
                FavoriteCharactersScreen(
                    uiState = uiState,
                    onCharacterClick = {},
                    onRemoveFavoriteClick = {},
                    onRetry = {},
                    onSearchQueryChange = { query ->
                        uiState = uiState.copy(searchQuery = query)
                    },
                )
            }
        }

        composeRule.onNodeWithText("Buscar favoritos").performTextInput("Sasuke")

        composeRule.onNodeWithText("Sasuke Uchiha").assertIsDisplayed()
        composeRule.onAllNodesWithText("Naruto Uzumaki").assertCountEquals(0)
    }

    @Test
    fun searchShowsEmptyFeedbackWhenNoFavoriteMatches() {
        val favorites = listOf(
            CharacterListItem(
                id = 1344,
                name = "Naruto Uzumaki",
                debut = CharacterDebut(manga = "Naruto Chapter #1"),
                isFavorite = true,
            ),
        )

        composeRule.setContent {
            var uiState by mutableStateOf(
                FavoriteCharactersUiState(
                    favorites = favorites,
                    isLoading = false,
                ),
            )

            DattebayoAppTheme {
                FavoriteCharactersScreen(
                    uiState = uiState,
                    onCharacterClick = {},
                    onRemoveFavoriteClick = {},
                    onRetry = {},
                    onSearchQueryChange = { query ->
                        uiState = uiState.copy(searchQuery = query)
                    },
                )
            }
        }

        composeRule.onNodeWithText("Buscar favoritos").performTextInput("Kakashi")

        composeRule.onNodeWithText("Nenhum favorito encontrado").assertIsDisplayed()
        composeRule.onNodeWithText("Tente outro termo de busca.").assertIsDisplayed()
    }
}
