package com.example.dattebayoapp.feature.characters.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.dattebayoapp.R
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.model.CharacterPersonal
import com.example.dattebayoapp.domain.model.CharacterRank
import com.example.dattebayoapp.domain.model.CharacterVoiceActors
import com.example.dattebayoapp.ui.theme.DattebayoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    character: CharacterDetails?,
    onBackClick: () -> Unit,
    onFavoriteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRetry: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = character?.name ?: stringResource(R.string.detail_default_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_content_description),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            isLoading -> CharacterLoadingState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            errorMessage != null -> CharacterFeedbackState(
                title = stringResource(R.string.detail_error_title),
                message = errorMessage,
                actionLabel = stringResource(R.string.retry),
                onAction = onRetry,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            character == null -> CharacterFeedbackState(
                title = stringResource(R.string.character_not_found_title),
                message = stringResource(R.string.character_not_found_message),
                actionLabel = stringResource(R.string.go_back),
                onAction = onBackClick,
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
                    CharacterHeroCard(
                        character = character,
                        onFavoriteClick = { onFavoriteClick(character.id) },
                    )
                }

                item {
                    CharacterSectionCard(
                        title = stringResource(R.string.debut_section),
                        content = {
                            CharacterKeyValue(stringResource(R.string.manga_label), character.debut.manga)
                            CharacterKeyValue(stringResource(R.string.anime_label), character.debut.anime)
                            CharacterKeyValue(stringResource(R.string.novel_label), character.debut.novel)
                            CharacterKeyValue(stringResource(R.string.movie_label), character.debut.movie)
                        },
                    )
                }

                if (character.family.isNotEmpty()) {
                    item {
                        CharacterSectionCard(
                            title = stringResource(R.string.family_section),
                            content = {
                                character.family.forEach { (label, value) ->
                                    CharacterKeyValue(label, value)
                                }
                            },
                        )
                    }
                }

                item {
                    CharacterSectionCard(
                        title = stringResource(R.string.profile_section),
                        content = {
                            CharacterKeyValue(stringResource(R.string.birthdate_label), character.personal.birthdate)
                            CharacterKeyValue(stringResource(R.string.sex_label), character.personal.sex)
                            CharacterKeyValue(stringResource(R.string.blood_type_label), character.personal.bloodType)
                            CharacterKeyValue(stringResource(R.string.status_label), character.personal.status)
                            CharacterKeyValue(stringResource(R.string.species_label), character.personal.species)
                            CharacterKeyValue(stringResource(R.string.registration_label), character.rank.ninjaRegistration)
                        },
                    )
                }

                if (character.natureType.isNotEmpty()) {
                    item {
                        CharacterTagSection(
                            title = stringResource(R.string.nature_types_section),
                            items = character.natureType,
                        )
                    }
                }

                if (character.personal.classification.isNotEmpty()) {
                    item {
                        CharacterTagSection(
                            title = stringResource(R.string.classification_section),
                            items = character.personal.classification,
                        )
                    }
                }

                if (character.personal.affiliation.isNotEmpty()) {
                    item {
                        CharacterTagSection(
                            title = stringResource(R.string.affiliation_section),
                            items = character.personal.affiliation,
                        )
                    }
                }

                if (character.jutsu.isNotEmpty()) {
                    item {
                        CharacterTagSection(
                            title = stringResource(R.string.jutsu_section),
                            items = character.jutsu.take(18),
                        )
                    }
                }

                if (character.tools.isNotEmpty()) {
                    item {
                        CharacterTagSection(
                            title = stringResource(R.string.tools_section),
                            items = character.tools,
                        )
                    }
                }

                if (character.voiceActors.japanese.isNotEmpty() || character.voiceActors.english.isNotEmpty()) {
                    item {
                        CharacterSectionCard(
                            title = stringResource(R.string.voice_actors_section),
                            content = {
                                CharacterValueList(stringResource(R.string.japanese_label), character.voiceActors.japanese)
                                CharacterValueList(stringResource(R.string.english_label), character.voiceActors.english)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterHeroCard(
    character: CharacterDetails,
    onFavoriteClick: () -> Unit,
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (character.images.isNotEmpty()) {
                AsyncImage(
                    model = character.images.first(),
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = character.name.take(1),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(start = 18.dp, end = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = stringResource(R.string.character_id, character.id),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    FilterChip(
                        selected = character.isFavorite,
                        onClick = onFavoriteClick,
                        label = {
                            Text(
                                if (character.isFavorite) {
                                    stringResource(R.string.favorite_selected)
                                } else {
                                    stringResource(R.string.favorite_action)
                                },
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(),
                    )
                }

                val heroHighlights = buildList {
                    character.personal.clan.firstOrNull()?.let { add(it) }
                    character.rank.ninjaRank.values.firstOrNull()?.let { add(it) }
                    character.personal.occupation.firstOrNull()?.let { add(it) }
                }

                if (heroHighlights.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        heroHighlights.forEach { label ->
                            CharacterInfoPill(label = label)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterSectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            content()
        }
    }
}

@Composable
private fun CharacterTagSection(
    title: String,
    items: List<String>,
) {
    CharacterSectionCard(title = title) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items.forEach { item ->
                CharacterInfoPill(label = item)
            }
        }
    }
}

@Composable
private fun CharacterKeyValue(
    label: String,
    value: String?,
) {
    if (value.isNullOrBlank()) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CharacterValueList(
    label: String,
    values: List<String>,
) {
    if (values.isEmpty()) return

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        values.forEach { value ->
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CharacterDetailScreenPreview() {
    DattebayoAppTheme {
        CharacterDetailScreen(
            character = CharacterDetails(
                id = 1344,
                name = "Naruto Uzumaki",
                images = listOf("https://static.wikia.nocookie.net/naruto/images/d/d6/Naruto_Part_I.png"),
                debut = CharacterDebut(
                    manga = "Naruto Chapter #1",
                    anime = "Naruto Episode #1",
                    movie = "Ninja Clash in the Land of Snow",
                ),
                isFavorite = true,
                family = mapOf(
                    "father" to "Minato Namikaze",
                    "mother" to "Kushina Uzumaki",
                ),
                jutsu = listOf("Rasengan", "Shadow Clone Technique", "Sage Mode"),
                natureType = listOf("Wind Release", "Lightning Release"),
                personal = CharacterPersonal(
                    birthdate = "October 10",
                    sex = "Male",
                    bloodType = "B",
                    classification = listOf("Jinchuriki", "Sage", "Sensor Type"),
                    occupation = listOf("Hokage"),
                    affiliation = listOf("Konohagakure", "Mount Myoboku"),
                    clan = listOf("Uzumaki"),
                ),
                rank = CharacterRank(
                    ninjaRank = mapOf("Part II" to "Kage"),
                    ninjaRegistration = "012607",
                ),
                tools = listOf("Flying Thunder God Kunai", "Konoha Chakra Blade"),
                voiceActors = CharacterVoiceActors(
                    japanese = listOf("Junko Takeuchi"),
                    english = listOf("Maile Flanagan"),
                ),
            ),
            onBackClick = {},
            onFavoriteClick = {},
        )
    }
}
