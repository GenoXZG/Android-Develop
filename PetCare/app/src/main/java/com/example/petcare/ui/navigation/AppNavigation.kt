package com.example.petcare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petcare.ui.screens.AboutScreen
import com.example.petcare.ui.screens.AddPetScreen
import com.example.petcare.ui.screens.HomeScreen
import com.example.petcare.ui.screens.PetDetailScreen
import com.example.petcare.viewmodel.PetViewModel

@Composable
fun AppNavigation(viewModel: PetViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable("add_pet") {
            AddPetScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable("about") {
            AboutScreen(navController = navController)
        }

        composable(
            route = "pet_detail/{petId}",
            arguments = listOf(
                navArgument("petId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable

            PetDetailScreen(
                petId = petId,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}