package com.demo.recipelist.ui.navigation

import androidx.collection.emptyLongSet
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.demo.recipelist.R
import com.demo.recipelist.ui.HomeDestination
import com.demo.recipelist.ui.HomeScreen
import com.demo.recipelist.ui.screen.DetailsDestination
import com.demo.recipelist.ui.screen.EditDestination
import com.demo.recipelist.ui.screen.InsertDestination
import com.demo.recipelist.ui.screen.MealPlannerDestination
import com.demo.recipelist.ui.screen.MealPlannerScreen
import com.demo.recipelist.ui.screen.PetsProfileDestination
import com.demo.recipelist.ui.screen.PetsProfileScreen
import com.demo.recipelist.ui.screen.RecipeDetailsScreen
import com.demo.recipelist.ui.screen.RecipeEditScreen
import com.demo.recipelist.ui.screen.RecipeInsertScreen
import com.demo.recipelist.ui.screen.SettingsDestination
import com.demo.recipelist.ui.screen.SettingsScreen
import com.demo.recipelist.ui.screen.UserProfileDestination
import com.demo.recipelist.ui.screen.UserProfileScreen

@Composable
fun RecipeListNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route
        // startDestination = TestDestination.route
    ) {
//        object TestDestination : NavigationDestination {
//            override val route = "test_screen"
//            override val titleRes = R.string.test_screen_title
//        }
//        composable(TestDestination.route) {
//            TestScreen()
//        }
        composable(HomeDestination.route) {
            HomeScreen(
                navigateToRecipeInsert = { navController.navigate(InsertDestination.route) },
                navigateToRecipeDetails = { navController.navigate("${DetailsDestination.route}/${it}") },
                navigateToUserProfile = { navController.navigate(UserProfileDestination.route) },
                navigateToPetsProfile = { navController.navigate(PetsProfileDestination.route) },
                navigateToMealPlanner = { navController.navigate(MealPlannerDestination.route) },
                navigateToSettings = { navController.navigate(SettingsDestination.route) }
            )
        }
        composable(UserProfileDestination.route) {
            UserProfileScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable(PetsProfileDestination.route) {
            PetsProfileScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable(MealPlannerDestination.route) {
            MealPlannerScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable(SettingsDestination.route) {
            SettingsScreen(onNavigateUp = { navController.navigateUp() })
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







