package com.example.dattebayoapp.core.di

import com.example.dattebayoapp.domain.usecase.GetCharacterDetailUseCase
import com.example.dattebayoapp.domain.usecase.GetCharactersUseCase
import com.example.dattebayoapp.domain.usecase.GetFavoriteCharactersUseCase
import com.example.dattebayoapp.domain.usecase.ObserveFavoriteCharacterIdsUseCase
import com.example.dattebayoapp.domain.usecase.ObserveFavoriteCharactersUseCase
import com.example.dattebayoapp.domain.usecase.ObserveFavoriteStatusUseCase
import com.example.dattebayoapp.domain.usecase.RemoveFavoriteUseCase
import com.example.dattebayoapp.domain.usecase.SaveFavoriteUseCase
import com.example.dattebayoapp.domain.usecase.ToggleFavoriteUseCase
import com.example.dattebayoapp.feature.characters.viewmodel.CharacterDetailViewModel
import com.example.dattebayoapp.feature.characters.viewmodel.CharacterViewModel
import com.example.dattebayoapp.feature.characters.viewmodel.FavoriteCharactersViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val useCaseModule = module {
    single { GetCharactersUseCase(get()) }
    single { GetCharacterDetailUseCase(get()) }
    single { GetFavoriteCharactersUseCase(get()) }
    single { ObserveFavoriteCharacterIdsUseCase(get()) }
    single { ObserveFavoriteCharactersUseCase(get()) }
    single { ObserveFavoriteStatusUseCase(get()) }
    single { SaveFavoriteUseCase(get()) }
    single { RemoveFavoriteUseCase(get()) }
    single { ToggleFavoriteUseCase(get()) }
}

val viewModelModule = module {
    viewModelOf(::CharacterViewModel)
    viewModelOf(::FavoriteCharactersViewModel)
    viewModelOf(::CharacterDetailViewModel)
}

val appModules: List<Module> = listOf(
    networkModule,
    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
)
