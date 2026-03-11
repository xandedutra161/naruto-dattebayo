package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.model.CharacterPage
import com.example.dattebayoapp.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(): CharacterPage {
        return characterRepository.getCharacters()
    }
}
