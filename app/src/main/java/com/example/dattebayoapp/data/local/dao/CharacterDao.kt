package com.example.dattebayoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.dattebayoapp.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY id ASC")
    suspend fun getCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY name ASC")
    suspend fun getFavoriteCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY name ASC")
    fun observeFavoriteCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT id FROM characters WHERE isFavorite = 1")
    suspend fun getFavoriteCharacterIds(): List<Int>

    @Query("SELECT id FROM characters WHERE isFavorite = 1")
    fun observeFavoriteCharacterIds(): Flow<List<Int>>

    @Query("SELECT isFavorite FROM characters WHERE id = :id")
    fun observeFavoriteStatus(id: Int): Flow<Boolean?>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean): Int

    @Upsert
    suspend fun upsertCharacters(characters: List<CharacterEntity>)

    @Upsert
    suspend fun upsertCharacter(character: CharacterEntity)
}
