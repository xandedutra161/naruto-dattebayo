package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.repository.CharacterRepository

class SaveFavoriteUseCase(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(id: Int): Boolean {
        return characterRepository.saveFavorite(id)
    }
}
