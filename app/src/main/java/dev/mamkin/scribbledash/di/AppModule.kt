package dev.mamkin.scribbledash.di

import dev.mamkin.scribbledash.data.repository.GameRepository
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.GameViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.DrawViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.PreviewViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results.ResultsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { GameViewModel(get()) }
    viewModel { (gameViewModel: GameViewModel) ->
        PreviewViewModel(gameViewModel)
    }
    viewModel { (gameViewModel: GameViewModel) ->
        DrawViewModel(gameViewModel)
    }
    viewModel { (gameViewModel: GameViewModel) ->
        ResultsViewModel(gameViewModel)
    }

    single { GameRepository(androidContext()) }
}