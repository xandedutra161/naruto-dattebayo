package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.repository.CharacterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteCharacterIdsUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    operator fun invoke(): Flow<Set<Int>> {
        return characterRepository.observeFavoriteCharacterIds()
    }
}
