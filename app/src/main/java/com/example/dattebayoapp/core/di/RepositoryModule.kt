package com.example.dattebayoapp.core.di

import com.example.dattebayoapp.data.repository.CharacterRepositoryImpl
import com.example.dattebayoapp.domain.repository.CharacterRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
}
