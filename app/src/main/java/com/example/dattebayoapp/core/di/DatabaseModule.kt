package com.example.dattebayoapp.core.di

import androidx.room.Room
import com.example.dattebayoapp.core.database.AppDatabase
import com.example.dattebayoapp.data.local.dao.CharacterDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME,
        ).build()
    }

    single<CharacterDao> { get<AppDatabase>().characterDao() }
}
