package com.demo.recipelist.ui.screen

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.data.Recipe
import com.demo.recipelist.ui.AppViewModelProvider
import com.demo.recipelist.ui.navigation.NavigationDestination
import com.demo.recipelist.ui.theme.RecipeListTheme
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
                navigateUp = onNavigateUp,
                deleteButton = true,
                onDeleteClick = {
                    /*TODO*/
                }
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
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit_screen_title),
                )
            }
        }
    ) {
        // Text(text = stringResource(R.string.edit_screen_title), modifier = Modifier.padding(it))
        RecipeDetailsBody(
            recipeDetails = uiState.value,
            ingList = viewModel.stringToList(uiState.value.ingredients),
            stepList = viewModel.stringToList(uiState.value.steps),
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
    ingList: List<String>,
    stepList: List<String>,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        item {
            Text(
                text = recipeDetails.title,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = modifier.padding(vertical = 12.dp)
            )
        }
        item {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(
                    text = recipeDetails.description,
                    modifier = modifier.padding(12.dp))
            }
        }
        item {
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "Time: ${recipeDetails.time} mins",
                    color = Color.Gray
                )
                Text(
                    text = "Servings: ${recipeDetails.servings}",
                    color = Color.Gray
                )
            }
            HorizontalDivider()
        }
        item {
            Text(
                text = stringResource(id = R.string.recipe_ingredients),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
        }
        items(ingList) {ing ->
            StringListRow(
                label = "ingredient",
                item = ing,
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.recipe_steps),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
        }
        itemsIndexed(stepList) {index, step ->
            StringListRow(
                label = "step",
                index = index,
                item = step,
            )
        }
        item {
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
}

@Composable
fun StringListRow(
    label: String = "",
    index: Int = 0,
    item: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = if (label == "step") "step ${index}: $item" else item,
            modifier = modifier.fillMaxWidth().padding(8.dp)
        )
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
    RecipeListTheme {
        RecipeDetailsBody(
            recipeDetails = RecipeDetails(
                id = 0,
                title = "食物標題",
                description = "作法描述 blabla...",
                time = "30",
                servings = "4",
                ingredients = "",
                steps = ""
            ),
            ingList = listOf("雞腿肉", "蔥 切段", "薑 切片", "蒜頭剝皮", "米酒適量", "喜歡的蔬菜"),
            stepList = listOf(
                "雞腿肉撒鹽巴、白胡椒粉醃製 10 分鐘",
                "滾水放入雞腿肉、蔥、薑、蒜、米酒，小火煮 10 分鐘",
                "10 分鐘後取出雞腿肉切塊",
                "雞湯放入喜歡的蔬菜川燙",
                "川燙後瀝出雞湯，蔬菜與雞腿肉放一起調味"
            ),
            onDelete = { }
        )
    }
}

//@Preview(apiLevel = 33, showBackground = true)
//@Composable
//fun RecipeStepsPreview() {
//    RecipeSteps(
//        Recipe(
//            id = 0,
//            title = "食物標題",
//            description = "作法描述 blabla...",
//            time = "30",
//            servings = 2
//        )
//    )
//}