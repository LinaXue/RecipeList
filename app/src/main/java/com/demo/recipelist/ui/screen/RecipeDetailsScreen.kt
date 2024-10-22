package com.demo.recipelist.ui.screen

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.data.Recipe
import com.demo.recipelist.ui.AppViewModelProvider
import com.demo.recipelist.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object DetailsDestination : NavigationDestination {
    override val route = "details_screen"
    override val titleRes = R.string.details_screen_title
    const val recipeIdArg = "recipeId"
    val routeWithArgs = "$route/{$recipeIdArg}"
}

@Composable
fun RecipeDetailsScreen(
    onNavigateUp: () -> Unit,
    navigateToRecipeUpdate: (Int) -> Unit,
    viewModel: RecipeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            RecipeListTopAppBar(
                title = stringResource(DetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToRecipeUpdate(uiState.value.id)
                    Log.d("DetailsScreen", "recipe id is ${uiState.value.id}")
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_screen_title),
                )
            }
        }
    ) {
        // Text(text = stringResource(R.string.edit_screen_title), modifier = Modifier.padding(it))
        RecipeDetailsBody(
            recipeDetails = uiState.value,
            onUpdateItem = {
                navigateToRecipeUpdate(uiState.value.id)
                Log.d("DetailsScreen", "recipe id is ${uiState.value.id}")
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteItem()
                    onNavigateUp()
                }
            },
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
private fun RecipeDetailsBody(
    recipeDetails: RecipeDetails,
    onUpdateItem: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        RecipeDetailsCard(
            recipe = recipeDetails.toRecipe(),
            modifier = Modifier.fillMaxWidth()
        )
        RecipeSteps(
            recipe = recipeDetails.toRecipe(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onUpdateItem,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = true
        ) {
            Text(stringResource(R.string.edit_button))
        }
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete_button))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun RecipeDetailsCard(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(modifier = modifier) {
                Text(text = stringResource(R.string.recipe_title))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = recipe.title, fontWeight = FontWeight.Bold)
            }
            Row(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
                Text(text = "Time: ${recipe.time} mins")
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "Servings: ${recipe.servings.toString()}")
            }

        }
    }
}

@Composable
fun RecipeSteps(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Text(text = stringResource(R.string.recipe_description))
        Text(text = recipe.description)
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        }
    )
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun RecipeDetailsCardPreview() {
    RecipeDetailsCard(
        Recipe(
            id = 0,
            title = "食物標題",
            description = "作法描述 blabla...",
            time = "30",
            servings = 2
        )
    )
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun RecipeStepsPreview() {
    RecipeSteps(
        Recipe(
            id = 0,
            title = "食物標題",
            description = "作法描述 blabla...",
            time = "30",
            servings = 2
        )
    )
}