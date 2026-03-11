package com.example.dattebayoapp.feature.characters.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.feature.characters.state.FavoriteCharactersUiState
import com.example.dattebayoapp.ui.theme.DattebayoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteCharactersScreen(
    uiState: FavoriteCharactersUiState,
    onCharacterClick: (Int) -> Unit,
    onRemoveFavoriteClick: (Int) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Favorites")
                        Text(
                            text = "${uiState.favorites.size} saved",
                            style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            uiState.isLoading -> CharacterLoadingState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            uiState.errorMessage != null -> CharacterFeedbackState(
                title = "Could not load favorites",
                message = uiState.errorMessage,
                actionLabel = "Try again",
                onAction = onRetry,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchQueryChange,
                        label = { Text("Search favorites") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (uiState.filteredFavorites.isEmpty()) {
                    item {
                        CharacterFeedbackState(
                            title = "No favorite matches",
                            message = if (uiState.searchQuery.isBlank()) {
                                "Favorite characters will appear here."
                            } else {
                                "Try a different search term."
                            },
                            actionLabel = "Refresh",
                            onAction = onRetry,
                        )
                    }
                } else {
                    items(
                        items = uiState.filteredFavorites,
                        key = { it.id },
                    ) { character ->
                        FavoriteCharacterCard(
                            character = character,
                            onClick = { onCharacterClick(character.id) },
                            onRemoveFavoriteClick = { onRemoveFavoriteClick(character.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteCharacterCard(
    character: CharacterListItem,
    onClick: () -> Unit,
    onRemoveFavoriteClick: () -> Unit,
) {
    androidx.compose.material3.ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = character.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            )

            character.debut?.appearsIn?.let { CharacterInfoPill(label = it) }

            Text(
                text = character.debut.toDebutLabel(),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Button(onClick = onRemoveFavoriteClick) {
                Text("Remove favorite")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteCharactersScreenPreview() {
    DattebayoAppTheme {
        FavoriteCharactersScreen(
            uiState = FavoriteCharactersUiState(
                favorites = listOf(
                    CharacterListItem(
                        id = 1344,
                        name = "Naruto Uzumaki",
                        debut = CharacterDebut(
                            manga = "Naruto Chapter #1",
                            anime = "Naruto Episode #1",
                            appearsIn = "Anime, Manga",
                        ),
                        isFavorite = true,
                    ),
                ),
            ),
            onCharacterClick = {},
            onRemoveFavoriteClick = {},
            onSearchQueryChange = {},
        )
    }
}
