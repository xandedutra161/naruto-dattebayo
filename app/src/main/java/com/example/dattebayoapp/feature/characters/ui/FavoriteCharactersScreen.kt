package com.example.dattebayoapp.feature.characters.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dattebayoapp.R
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
                        Text(text = stringResource(R.string.favorites_title))
                        Text(
                            text = stringResource(R.string.favorites_saved, uiState.favorites.size),
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
                title = stringResource(R.string.favorites_error_title),
                message = uiState.errorMessage,
                actionLabel = stringResource(R.string.retry),
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
                        label = { Text(stringResource(R.string.search_favorites_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (uiState.filteredFavorites.isEmpty()) {
                    item {
                        CharacterFeedbackState(
                            title = stringResource(R.string.favorites_empty_title),
                            message = if (uiState.searchQuery.isBlank()) {
                                stringResource(R.string.favorites_empty_message)
                            } else {
                                stringResource(R.string.favorites_search_empty_message)
                            },
                            actionLabel = stringResource(R.string.refresh),
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
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            CharacterImage(
                imageUrl = character.images.firstOrNull(),
                contentDescription = character.name,
                modifier = Modifier.size(width = 92.dp, height = 112.dp),
            )

            Column(
                modifier = Modifier.weight(1f),
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
                    Text(stringResource(R.string.remove_favorite_action))
                }
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
