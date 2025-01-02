package com.cydonic.nvs.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cydonic.nvs.com.cydonic.nvs.ui.projectscreen.ProjectScreenViewModel
import com.cydonic.nvs.com.cydonic.nvs.ui.projectscreen.ProjectScreenViewModelFactory
import com.cydonic.nvs.ui.projectscreen.ProjectScreen
import com.cydonic.nvs.ui.startupscreen.StartupScreen
import com.cydonic.nvs.ui.startupscreen.StartupScreenViewModel
import com.cydonic.nvs.ui.startupscreen.StartupScreenViewModelFactory
import java.io.File

class StartupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nvsLocalDirectory = File(filesDir, "nVS")
        if (!nvsLocalDirectory.exists()) {
            nvsLocalDirectory.mkdirs()
        }
        System.setProperty("user.dir", nvsLocalDirectory.absolutePath)

        setContent {

            val navController = rememberNavController()

            val viewModel: StartupScreenViewModel by viewModels { StartupScreenViewModelFactory(nvsLocalDirectory) }


            NavHost(navController = navController, startDestination = "startupScreen") {
                composable("startupScreen") {
                    StartupScreen(
                        viewModel = viewModel,
                        navController = navController,
                        onCloneRepoClick = { /* Handle sync with GitHub */ }


                    )
                }

                // Define ProjectScreen route and argument handling
                composable(
                    route = "projectScreen/{folderName}",
                    arguments = listOf(navArgument("folderName") {
                        type =
                            NavType.StringType
                    })
                ) { backStackEntry ->
                    val folderName = backStackEntry.arguments?.getString("folderName") ?: ""
                    val folder = File(folderName)
                    val viewModel:  ProjectScreenViewModel =
                        viewModel(factory = ProjectScreenViewModelFactory(folder))
                    ProjectScreen(
                        folderName = folderName,
                        onNavigateBack = { navController.popBackStack() },
                        viewModel = viewModel
                    )
                }
            }



        }
    }
}