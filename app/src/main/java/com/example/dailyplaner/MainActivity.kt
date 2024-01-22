package com.example.dailyplaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dailyplaner.presentation.navigation.AppState
import com.example.dailyplaner.presentation.navigation.Destinations
import com.example.dailyplaner.presentation.screens.planner.PlannerScreen
import com.example.dailyplaner.presentation.screens.planner.PlannerViewModel
import com.example.dailyplaner.presentation.screens.to_do_info.ToDoInfoScreen
import com.example.dailyplaner.presentation.screens.to_do_info.ToDoInfoViewModel
import com.example.dailyplaner.presentation.ui.theme.DailyPlanerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberAppState()
            DailyPlanerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        NavHost(
                            navController = appState.navController,
                            startDestination = Destination.Planner.routeStartName,
                            modifier = Modifier.padding(it)
                        ) {
                            navGraph(
                                upPress = appState::upPress,
                                createToDoClick = appState::navigateToToDoInfoScreenCreate,
                                onToDoClick = appState::navigateToToDoInfoScreenUpdate
                            )
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun rememberAppState(
        navController: NavHostController = rememberNavController()
    ) = remember(navController) {
        AppState(navController)
    }

    private fun NavGraphBuilder.navGraph(
        upPress: () -> Unit,
        createToDoClick: (Long, NavBackStackEntry) -> Unit,
        onToDoClick: (Int, NavBackStackEntry) -> Unit,
    ) {
        composable(Destinations.PLANNER) {
            val viewModel: PlannerViewModel = viewModel(factory = PlannerViewModel.Factory)
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            PlannerScreen(
                state.value,
                viewModel::onEvent,
                { dateInMillis -> createToDoClick(dateInMillis, it) },
                { id -> onToDoClick(id, it) }
            )
        }
        composable(
            "${Destinations.PLANNER}/${Destinations.TO_DO_INFO_ROUTE}" +
                    "?${Destinations.TO_DO_INFO_ID}={${Destinations.TO_DO_INFO_ID}}" +
                    "&${Destinations.TO_DO_INFO_DATE}={${Destinations.TO_DO_INFO_DATE}}",
            arguments = listOf(
                navArgument(Destinations.TO_DO_INFO_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(Destinations.TO_DO_INFO_DATE) {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            val viewModel: ToDoInfoViewModel = viewModel(factory = ToDoInfoViewModel.Factory)
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            ToDoInfoScreen(
                state.value,
                viewModel::onEvent,
                upPress
            )
        }
    }
}












