package dev.mamkin.scribbledash.di

import StatisticsRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.mamkin.scribbledash.data.repository.GameRepository
import dev.mamkin.scribbledash.dataStore
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderViewModel
import dev.mamkin.scribbledash.presentation.screens.speedDraw.SpeedDrawViewModel
import dev.mamkin.scribbledash.presentation.screens.statistics.StatisticsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val USER_PREFERENCES_NAME = "user_statistics"

val appModule = module {
    // DataStore setup
    single<DataStore<Preferences>> { androidApplication().dataStore }

    // Repositories
    single { GameRepository(androidContext()) }
    singleOf(::StatisticsRepository) // Эквивалентно single { StatisticsRepository(get()) }

    // ViewModels
    viewModel { SpeedDrawViewModel(get(), get()) } // Добавляем StatisticsRepository
    viewModel { OneRoundWonderViewModel(get()) }
    viewModel { StatisticsViewModel(get()) } // Регистрируем ViewModel
}
