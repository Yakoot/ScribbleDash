package dev.mamkin.scribbledash.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.generated.destinations.StatisticsRootDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import com.ramcosta.composedestinations.utils.startDestination
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.theme.Primary
import dev.mamkin.scribbledash.ui.theme.TertiaryContainer

@Composable
fun AppBottomBar(
    navController: NavHostController
) {
    val currentDestination: DestinationSpec = navController.currentDestinationAsState().value
        ?: NavGraphs.root.startDestination

    val destinationsNavigator = navController.rememberDestinationsNavigator()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        BottomBarDestination.entries.forEach { destination ->
            val isSelected = currentDestination == destination.direction
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIconColor = destination.selectedIconColor,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = null,
                    )
                },
                selected = isSelected,
                enabled = destination.enabled,
                onClick = {
                    destinationsNavigator.navigate(destination.direction) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: Int,
    val selectedIconColor: Color,
    val enabled: Boolean = true,
) {
    ComingSoon(StatisticsRootDestination, R.drawable.ic_statistics,
        TertiaryContainer
    ),
    Home(HomeRootDestination, R.drawable.ic_home, Primary),
}
