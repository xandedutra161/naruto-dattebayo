package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.repository.CharacterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteStatusUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    operator fun invoke(id: Int): Flow<Boolean> {
        return characterRepository.observeFavoriteStatus(id)
    }
}
