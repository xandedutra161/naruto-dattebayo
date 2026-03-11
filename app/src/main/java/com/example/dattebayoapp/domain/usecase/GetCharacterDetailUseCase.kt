package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.model.CharacterDetails
import com.example.dattebayoapp.domain.repository.CharacterRepository

class GetCharacterDetailUseCase(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(id: Int): CharacterDetails {
        return characterRepository.getCharacterDetails(id)
    }
}
