package com.example.dattebayoapp.domain.usecase

import com.example.dattebayoapp.domain.model.CharacterListItem
import com.example.dattebayoapp.domain.repository.CharacterRepository
import javax.inject.Inject

class GetFavoriteCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(): List<CharacterListItem> {
        return characterRepository.getFavoriteCharacters()
    }
}
