package dev.mamkin.scribbledash.di

import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.GameViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.DrawViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.PreviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::DrawViewModel)
    viewModelOf(::GameViewModel)
    viewModelOf(::PreviewViewModel)
}