package dev.mamkin.scribbledash.di

import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.ImagesViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.DrawViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::DrawViewModel)
    singleOf(::ImagesViewModel)
}