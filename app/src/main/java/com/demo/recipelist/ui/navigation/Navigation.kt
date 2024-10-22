package com.demo.recipelist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.demo.recipelist.ui.HomeDestination
import com.demo.recipelist.ui.HomeScreen
import com.demo.recipelist.ui.screen.DetailsDestination
import com.demo.recipelist.ui.screen.EditDestination
import com.demo.recipelist.ui.screen.InsertDestination
import com.demo.recipelist.ui.screen.RecipeDetailsScreen
import com.demo.recipelist.ui.screen.RecipeEditScreen
import com.demo.recipelist.ui.screen.RecipeInsertScreen

@Composable
fun RecipeListNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route
    ) {
        composable(HomeDestination.route) {
            HomeScreen(
                navigateToRecipeInsert = { navController.navigate(InsertDestination.route) },
                navigateToRecipeDetails = { navController.navigate("${DetailsDestination.route}/${it}") }
            )
        }
        composable(InsertDestination.route) {
            RecipeInsertScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = DetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailsDestination.recipeIdArg) {
                type = NavType.IntType
            })
        ) {
            RecipeDetailsScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToRecipeUpdate = { navController.navigate("${EditDestination.route}/${it}") }
            )
        }
        composable(
            route = EditDestination.routeWithArgs,
            arguments = listOf(navArgument(EditDestination.recipeIdArg) {
                type = NavType.IntType
            })
        ) {
            RecipeEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}







