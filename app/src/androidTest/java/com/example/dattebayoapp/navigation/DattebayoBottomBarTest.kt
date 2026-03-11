package com.example.dattebayoapp.navigation

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.dattebayoapp.ui.theme.DattebayoAppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DattebayoBottomBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun bottomBarShowsMainDestinations() {
        composeRule.setContent {
            DattebayoAppTheme {
                DattebayoBottomBar(
                    currentRoute = AppDestination.Characters.route,
                    destinations = listOf(
                        AppDestination.Characters,
                        AppDestination.Favorites,
                    ),
                    onNavigate = {},
                )
            }
        }

        composeRule.onNodeWithText("Characters").assertExists().assertIsSelected()
        composeRule.onNodeWithText("Favorites").assertExists()
        composeRule.onNodeWithContentDescription("Characters").assertExists()
        composeRule.onNodeWithContentDescription("Favorites").assertExists()
    }

    @Test
    fun bottomBarInvokesNavigationWhenItemIsClicked() {
        var selectedRoute = AppDestination.Characters.route

        composeRule.setContent {
            DattebayoAppTheme {
                DattebayoBottomBar(
                    currentRoute = selectedRoute,
                    destinations = listOf(
                        AppDestination.Characters,
                        AppDestination.Favorites,
                    ),
                    onNavigate = { destination ->
                        selectedRoute = destination.route
                    },
                )
            }
        }

        composeRule.onNodeWithText("Favorites").performClick()

        assertEquals(AppDestination.Favorites.route, selectedRoute)
    }
}
