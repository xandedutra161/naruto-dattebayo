package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.repository.CharacterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    operator fun invoke(): Flow<List<CharacterListItem>> {
        return characterRepository.observeFavoriteCharacters()
    }
}
