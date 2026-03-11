package com.example.dattebayoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.dattebayoapp.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY id ASC")
    suspend fun getCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY name ASC")
    suspend fun getFavoriteCharacters(): List<CharacterEntity>

    @Query("SELECT id FROM characters WHERE isFavorite = 1")
    suspend fun getFavoriteCharacterIds(): List<Int>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean): Int

    @Upsert
    suspend fun upsertCharacters(characters: List<CharacterEntity>)

    @Upsert
    suspend fun upsertCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters")
    suspend fun clearCharacters()
}
