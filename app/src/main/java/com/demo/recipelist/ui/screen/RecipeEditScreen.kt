package com.demo.recipelist.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.ui.AppViewModelProvider
import com.demo.recipelist.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object EditDestination : NavigationDestination {
    override val route = "edit_screen"
    override val titleRes = R.string.edit_screen_title
    const val recipeIdArg = "recipeId"
    val routeWithArgs = "$route/{$recipeIdArg}"
}

@Composable
fun RecipeEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: RecipeEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            RecipeListTopAppBar(
                title = stringResource(EditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            ) }
    ) {
        RecipeInsertBody(
            recipeUiState = viewModel.recipeUiState,
            onRecipeValueChange = viewModel::updateUiState,
            onSaveClicked = {
                coroutineScope.launch {
                    viewModel.updateRecipe()
                    navigateBack()
                }
            },
            paddingValues = it
        )
    }
}