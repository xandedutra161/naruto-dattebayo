package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.repository.CharacterRepository

class GetCharactersUseCase(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(): CharacterPage {
        return characterRepository.getCharacters()
    }
}
