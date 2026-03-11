package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteCharacterIdsUseCase(
    private val characterRepository: CharacterRepository,
) {
    operator fun invoke(): Flow<Set<Int>> {
        return characterRepository.observeFavoriteCharacterIds()
    }
}
