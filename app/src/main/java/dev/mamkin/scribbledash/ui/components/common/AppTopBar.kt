package dev.mamkin.scribbledash.ui.components.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    titleCentered: Boolean = false,
    title: @Composable () -> Unit = {},
    timer: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    if (titleCentered) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = title,
            actions = actions,
            navigationIcon = timer,
            windowInsets = WindowInsets(0, 0, 0, 0),
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent
            )
        )
    } else {
        TopAppBar(
            modifier = modifier,
            title = title,
            actions = actions,
            navigationIcon = timer,
            windowInsets = WindowInsets(0, 0, 0, 0),
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent
            )
        )
    }
}