package dev.mamkin.scribbledash

import android.content.Context
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.generated.destinations.StatisticsRootDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination
import dev.mamkin.scribbledash.di.appModule
import dev.mamkin.scribbledash.ui.components.common.AppBottomBar
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_statistics")

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
                    listOf(
                        StatisticsRootDestination.route,
                        HomeRootDestination.route
                    ).contains(currentDestination.route)
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
//                        dependenciesContainerBuilder = {
//                            navGraph(NavGraphs.oneRoundWonder) {
//                                val parentEntry = remember(navBackStackEntry) {
//                                    navController.getBackStackEntry(NavGraphs.oneRoundWonder.route)
//                                }
//                                dependency(koinViewModel<OneRoundWonderViewModelOld>(viewModelStoreOwner = parentEntry))
//                            }
//                            navGraph(NavGraphs.speedDraw) {
//                                val parentEntry = remember(navBackStackEntry) {
//                                    navController.getBackStackEntry(NavGraphs.speedDraw.route)
//                                }
//                                dependency(koinViewModel<SpeedDrawViewModel>(viewModelStoreOwner = parentEntry))
//                            }
//                            navGraph(NavGraphs.endlessMode) {
//                                val parentEntry = remember(navBackStackEntry) {
//                                    navController.getBackStackEntry(NavGraphs.endlessMode.route)
//                                }
//                                dependency(koinViewModel<EndlessModeViewModel>(viewModelStoreOwner = parentEntry))
//                            }
//                        }
                    )
                }
            }
        }
    }
}
