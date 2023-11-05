package com.mikolaj.solvro.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mikolaj.solvro.R
import com.mikolaj.solvro.ui.add.AddTask
import com.mikolaj.solvro.ui.edit.EditScreen
import com.mikolaj.solvro.ui.home.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(
    navController: NavHostController = rememberNavController()
) {


    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        backStackEntry?.destination?.route ?: Screen.Home.route

    Scaffold(
        floatingActionButton = {
            if (currentScreen == Screen.Home.route) {
                FloatingActionButton(onClick = { navController.navigate(Screen.AddTask.route) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_task)
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (currentScreen != Screen.Home.route) {
                        IconButton(onClick = navController::navigateUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.add_task)
                            )
                        }
                    }
                },
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(
                    navigateToEditTask = {
                        navController.navigate(
                            route = Screen.EditTask.route.replace(
                                oldValue = "{taskId}",
                                newValue = it
                            )
                        )
                    }
                )
            }
            composable(route = Screen.AddTask.route) {
                AddTask(navController::navigateUp)
            }
            composable(route = Screen.EditTask.route) {
                EditScreen(navController::navigateUp)
            }
        }
    }

}