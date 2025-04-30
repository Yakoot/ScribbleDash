package dev.mamkin.scribbledash.di

import dev.mamkin.scribbledash.data.repository.GameRepository
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.DrawViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.PreviewViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results.ResultsViewModel
import dev.mamkin.scribbledash.presentation.screens.speedDraw.SpeedDrawViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { OneRoundWonderViewModel(get()) }
    viewModel { SpeedDrawViewModel() }
    viewModel { EndlessModeViewModel() }
    viewModel { (oneRoundWonderViewModel: OneRoundWonderViewModel) ->
        PreviewViewModel(oneRoundWonderViewModel)
    }
    viewModel { (oneRoundWonderViewModel: OneRoundWonderViewModel) ->
        DrawViewModel(oneRoundWonderViewModel)
    }
    viewModel { (oneRoundWonderViewModel: OneRoundWonderViewModel) ->
        ResultsViewModel(oneRoundWonderViewModel)
    }

    single { GameRepository(androidContext()) }
}
