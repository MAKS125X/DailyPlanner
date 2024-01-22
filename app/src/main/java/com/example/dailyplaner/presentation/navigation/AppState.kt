package com.example.dailyplaner.presentation.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController

class AppState(
    val navController: NavHostController
) {
    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToToDoInfoScreenCreate(dateInMillis: Long, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${Destinations.PLANNER}/${Destinations.TO_DO_INFO_ROUTE}?${Destinations.TO_DO_INFO_DATE}=$dateInMillis")
        }
    }

    fun navigateToToDoInfoScreenUpdate(toDoId: Int, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${Destinations.PLANNER}/${Destinations.TO_DO_INFO_ROUTE}?${Destinations.TO_DO_INFO_ID}=$toDoId")
        }
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}