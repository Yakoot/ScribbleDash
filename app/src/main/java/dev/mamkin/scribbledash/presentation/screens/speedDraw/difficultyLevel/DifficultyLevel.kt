package dev.mamkin.scribbledash.presentation.screens.speedDraw.difficultyLevel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.generated.destinations.PreviewRootDestination
import com.ramcosta.composedestinations.generated.destinations.PreviewRootDestination.invoke
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeGraph
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderGraph
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderViewModel
import dev.mamkin.scribbledash.presentation.screens.speedDraw.SpeedDrawGraph
import dev.mamkin.scribbledash.presentation.screens.speedDraw.SpeedDrawViewModel
import dev.mamkin.scribbledash.ui.components.DifficultyLevelScreen

@Destination<SpeedDrawGraph>(
    start = true,
    route = "speed_draw_difficulty_level_root"
)
@Composable
fun DifficultyLevelRoot(
    speedDrawViewModel: SpeedDrawViewModel,
    navigator: DestinationsNavigator,
) {
    LaunchedEffect(Unit) {
//        endlessModeViewModel.preloadImagesToCache()
    }
    DifficultyLevelScreen(
        onClose = {
            navigator.popBackStack(
                route = HomeRootDestination,
                inclusive = false
            )
        },
        onLevelClick = {
//            oneRoundWonderViewModel.setDifficultyLevel(it)
//            navigator.navigate(PreviewRootDestination())
        }
    )
}
