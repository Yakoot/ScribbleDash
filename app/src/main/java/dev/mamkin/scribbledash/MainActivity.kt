package dev.mamkin.scribbledash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.navgraphs.OneRoundWonderNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navGraph
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.contains
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination
import dev.mamkin.scribbledash.di.appModule
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderViewModel
import dev.mamkin.scribbledash.presentation.screens.speedDraw.SpeedDrawViewModel
import dev.mamkin.scribbledash.ui.components.AppBottomBar
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startKoin {
            androidContext(application)
            modules(appModule)
        }
        setContent {
            ScribbleDashTheme {
                val navController = rememberNavController()

                val currentDestination: DestinationSpec =
                    navController.currentDestinationAsState().value
                        ?: NavGraphs.root.startDestination

                val isBottomBarVisible = remember(currentDestination) {
                    !OneRoundWonderNavGraph.contains(currentDestination)
                }

                Scaffold(
                    bottomBar = {
                        if (isBottomBarVisible) {
                            AppBottomBar(navController)
                        }
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { contentPadding ->
                    DestinationsNavHost(
                        modifier = Modifier
                            .padding(contentPadding),
                        navController = navController,
                        navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            navGraph(NavGraphs.oneRoundWonder) {
                                val parentEntry = remember(navBackStackEntry) {
                                    navController.getBackStackEntry(NavGraphs.oneRoundWonder.route)
                                }
                                dependency(koinViewModel<OneRoundWonderViewModel>(viewModelStoreOwner = parentEntry))
                            }
                            navGraph(NavGraphs.speedDraw) {
                                val parentEntry = remember(navBackStackEntry) {
                                    navController.getBackStackEntry(NavGraphs.speedDraw.route)
                                }
                                dependency(koinViewModel<SpeedDrawViewModel>(viewModelStoreOwner = parentEntry))
                            }
                            navGraph(NavGraphs.endlessMode) {
                                val parentEntry = remember(navBackStackEntry) {
                                    navController.getBackStackEntry(NavGraphs.endlessMode.route)
                                }
                                dependency(koinViewModel<EndlessModeViewModel>(viewModelStoreOwner = parentEntry))
                            }
                        }
                    )
                }
            }
        }
    }
}
