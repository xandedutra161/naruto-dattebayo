package com.example.dattebayoapp.core.database

import androidx.room.TypeConverter
import com.example.dattebayoapp.domain.model.CharacterDebut
import com.example.dattebayoapp.domain.model.CharacterPersonal
import com.example.dattebayoapp.domain.model.CharacterRank
import com.example.dattebayoapp.domain.model.CharacterVoiceActors
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DatabaseConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return gson.fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun fromStringMap(value: Map<String, String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringMap(value: String?): Map<String, String> {
        if (value.isNullOrBlank()) return emptyMap()
        return gson.fromJson(value, object : TypeToken<Map<String, String>>() {}.type)
    }

    @TypeConverter
    fun fromCharacterDebut(value: CharacterDebut): String = gson.toJson(value)

    @TypeConverter
    fun toCharacterDebut(value: String?): CharacterDebut {
        if (value.isNullOrBlank()) return CharacterDebut()
        return gson.fromJson(value, CharacterDebut::class.java)
    }

    @TypeConverter
    fun fromCharacterPersonal(value: CharacterPersonal): String = gson.toJson(value)

    @TypeConverter
    fun toCharacterPersonal(value: String?): CharacterPersonal {
        if (value.isNullOrBlank()) return CharacterPersonal()
        return gson.fromJson(value, CharacterPersonal::class.java)
    }

    @TypeConverter
    fun fromCharacterRank(value: CharacterRank): String = gson.toJson(value)

    @TypeConverter
    fun toCharacterRank(value: String?): CharacterRank {
        if (value.isNullOrBlank()) return CharacterRank()
        return gson.fromJson(value, CharacterRank::class.java)
    }

    @TypeConverter
    fun fromCharacterVoiceActors(value: CharacterVoiceActors): String = gson.toJson(value)

    @TypeConverter
    fun toCharacterVoiceActors(value: String?): CharacterVoiceActors {
        if (value.isNullOrBlank()) return CharacterVoiceActors()
        return gson.fromJson(value, CharacterVoiceActors::class.java)
    }
}
