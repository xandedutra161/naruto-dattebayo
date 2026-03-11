package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.repository.CharacterRepository
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(id: Int): Boolean {
        return characterRepository.removeFavorite(id)
    }
}
