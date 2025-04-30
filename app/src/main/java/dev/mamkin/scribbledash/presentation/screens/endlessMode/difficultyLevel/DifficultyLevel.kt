package dev.mamkin.scribbledash.presentation.screens.endlessMode.difficultyLevel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeGraph
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderGraph
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderViewModel
import dev.mamkin.scribbledash.presentation.screens.speedDraw.SpeedDrawGraph
import dev.mamkin.scribbledash.ui.components.DifficultyLevelScreen

@Destination<EndlessModeGraph>(
    start = true,
    route = "endless_mode_difficulty_level_root"
)
@Composable
fun DifficultyLevelRoot(
    endlessModeViewModel: EndlessModeViewModel,
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
