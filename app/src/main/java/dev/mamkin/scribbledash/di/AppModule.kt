package dev.mamkin.scribbledash.di

import dev.mamkin.scribbledash.data.repository.GameRepository
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderViewModel
import dev.mamkin.scribbledash.presentation.screens.speedDraw.SpeedDrawViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SpeedDrawViewModel() }
    viewModel { OneRoundWonderViewModel(get()) }
    single { GameRepository(androidContext()) }
}
