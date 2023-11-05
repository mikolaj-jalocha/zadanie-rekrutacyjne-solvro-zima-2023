package com.mikolaj.solvro.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object AddTask : Screen(route = "add_task")
    object EditTask : Screen(route = "edit_task/{taskId}")
}
