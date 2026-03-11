package com.example.dattebayoapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dattebayoapp.R
import com.example.dattebayoapp.feature.characters.ui.CharacterDetailScreen
import com.example.dattebayoapp.feature.characters.ui.CharacterListScreen
import com.example.dattebayoapp.feature.characters.ui.FavoriteCharactersScreen
import com.example.dattebayoapp.feature.characters.viewmodel.CharacterDetailViewModel
import com.example.dattebayoapp.feature.characters.viewmodel.CharacterViewModel
import com.example.dattebayoapp.feature.characters.viewmodel.FavoriteCharactersViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.viewmodel.koinNavViewModel

@Composable
fun DattebayoNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val bottomDestinations = listOf(
        AppDestination.Characters,
        AppDestination.Favorites,
    )
    val shouldShowBottomBar = bottomDestinations.any { destination ->
        currentDestination?.hierarchy?.any { it.route == destination.route } == true
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (shouldShowBottomBar) {
                DattebayoBottomBar(
                    currentRoute = currentDestination?.route,
                    destinations = bottomDestinations,
                    onNavigate = { destination ->
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Characters.route,
            modifier = Modifier,
        ) {
            composable(AppDestination.Characters.route) {
                val viewModel: CharacterViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CharacterListScreen(
                    characters = uiState.characters,
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    onRetry = viewModel::loadCharacters,
                    onFavoriteClick = viewModel::toggleFavorite,
                    onCharacterClick = { characterId ->
                        navController.navigate(AppDestination.CharacterDetail.createRoute(characterId))
                    },
                    modifier = Modifier.padding(innerPadding),
                )
            }

            composable(AppDestination.Favorites.route) {
                val viewModel: FavoriteCharactersViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                FavoriteCharactersScreen(
                    uiState = uiState,
                    onRetry = viewModel::loadFavorites,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onRemoveFavoriteClick = viewModel::removeFavorite,
                    onCharacterClick = { characterId ->
                        navController.navigate(AppDestination.CharacterDetail.createRoute(characterId))
                    },
                    modifier = Modifier.padding(innerPadding),
                )
            }

            composable(
                route = AppDestination.CharacterDetail.route,
                arguments = listOf(
                    navArgument("characterId") { type = NavType.IntType },
                ),
            ) {
                val viewModel: CharacterDetailViewModel = koinNavViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CharacterDetailScreen(
                    character = uiState.character,
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    onRetry = viewModel::loadCharacter,
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onBackClick = { navController.popBackStack() },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@Composable
fun DattebayoBottomBar(
    currentRoute: String?,
    destinations: List<AppDestination>,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val labelResId = destination.labelResId
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = { onNavigate(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(labelResId),
                    )
                },
                label = { Text(stringResource(labelResId)) },
            )
        }
    }
}

private val AppDestination.icon: ImageVector
    get() = when (this) {
        AppDestination.Characters -> Icons.Outlined.Groups
        AppDestination.Favorites -> Icons.Outlined.FavoriteBorder
        AppDestination.CharacterDetail -> Icons.Outlined.Groups
    }

private val AppDestination.labelResId: Int
    get() = when (this) {
        AppDestination.Characters -> R.string.nav_characters
        AppDestination.Favorites -> R.string.nav_favorites
        AppDestination.CharacterDetail -> R.string.detail_default_title
    }
