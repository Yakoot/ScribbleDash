package dev.mamkin.scribbledash.di

import ShopRepository
import StatisticsRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.mamkin.scribbledash.data.repository.ImagesRepository
import dev.mamkin.scribbledash.dataStore
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderViewModel
import dev.mamkin.scribbledash.presentation.screens.shop.ShopViewModel
import dev.mamkin.scribbledash.presentation.screens.speedDraw.SpeedDrawViewModel
import dev.mamkin.scribbledash.presentation.screens.statistics.StatisticsViewModel
import dev.mamkin.scribbledash.ui.components.draw.DrawViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private const val USER_PREFERENCES_NAME = "user_statistics"

val appModule = module {
    // DataStore setup
    single<DataStore<Preferences>> { androidApplication().dataStore }

    // Repositories
    single { ImagesRepository(androidContext()) }
    singleOf(::StatisticsRepository)
    singleOf(::ShopRepository)

    // ViewModels
    viewModel { SpeedDrawViewModel(get(), get(), get()) }
    viewModel { EndlessModeViewModel(get(), get(), get()) }
    viewModel { OneRoundWonderViewModel(get(), get()) }
    viewModel { StatisticsViewModel(get()) }
    viewModelOf(::DrawViewModel)
    viewModelOf(::ShopViewModel)
}
