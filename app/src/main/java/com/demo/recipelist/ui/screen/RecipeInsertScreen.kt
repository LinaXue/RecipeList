package com.demo.recipelist.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.ui.AppViewModelProvider
import com.demo.recipelist.ui.navigation.NavigationDestination
import com.demo.recipelist.ui.theme.RecipeListTheme
import kotlinx.coroutines.launch

object InsertDestination : NavigationDestination {
    override val route = "insert_screen"
    override val titleRes = R.string.insert_screen_title
}

@Composable
fun RecipeInsertScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: RecipeInsertViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            RecipeListTopAppBar(
                title = stringResource(InsertDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.insertRecipe()
                        navigateBack()
                    }
                },
            ) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Add the recipe.")
            }
        }
    ) {
        RecipeInsertBody(
            recipeUiState = viewModel.recipeUiState,
            onRecipeValueChange = viewModel::updateRecipeUiState,
            ingredientsUiState = viewModel.ingredientUiState,
            onIngredientValueChange = viewModel::updateIngredientUiState,
            onIngredientRemove = viewModel::deleteIngredientUiState,
            stepUiState = viewModel.stepUiState,
            onStepValueChange = viewModel::updateStepUiState,
            onStepRemove = viewModel::deleteStepUiState,
            onSaveClicked = {
                coroutineScope.launch {
                    viewModel.insertRecipe()
                    navigateBack()
                }
            },
            paddingValues = it
        )
    }
}

//@Composable
//fun RecipeInsertBodyOld(
//    recipeUiState: RecipeUiState,
//    onRecipeValueChange: (RecipeDetails) -> Unit,
//    onSaveClicked: () -> Unit,
//    modifier: Modifier = Modifier,
//    paddingValues: PaddingValues
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = modifier.padding(paddingValues)
//            //.verticalScroll(rememberScrollState())
//    ) {
//        RecipeInputForm(
//            recipeDetails = recipeUiState.recipeDetails,
//            onValueChange = onRecipeValueChange,
//        )
//        Spacer(modifier = modifier.height(16.dp))
//        // DraggableList("食材")
//        Spacer(modifier = modifier.height(16.dp))
//        Button(
//            onClick = onSaveClicked,
//            enabled = recipeUiState.isInputValid,
//            shape = MaterialTheme.shapes.small
//        ) {
//            Text(text = stringResource(id = R.string.save_button))
//        }
//    }
//}

@Composable
fun RecipeInsertBody(
    recipeUiState: RecipeUiState,
    onRecipeValueChange: (RecipeDetails) -> Unit,
    ingredientsUiState: SnapshotStateList<String>,
    onIngredientValueChange: (Int, String) -> Unit,
    onIngredientRemove: (String) -> Unit,
    stepUiState: SnapshotStateList<String>,
    onStepValueChange: (Int, String) -> Unit,
    onStepRemove: (String) -> Unit,
    onSaveClicked: () -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = paddingValues
    ) {
        item {
            RecipeInputForm(
                recipeDetails = recipeUiState.recipeDetails,
                onValueChange = onRecipeValueChange,
                modifier = modifier.fillMaxWidth()
            )
        }
        item {
            HorizontalDivider()
            ListTitleRow(
                title = stringResource(id = R.string.recipe_ingredients),
                stringUiState = ingredientsUiState,
                modifier = modifier.fillMaxWidth()
            )
        }
        itemsIndexed(ingredientsUiState) { index, item ->
            DraggableListRow(
                label = "Ingredient",
                value = item,
                onValueChange = { onIngredientValueChange(index, it) },
                onButtonClick = { onIngredientRemove(item) },
                modifier = modifier.fillMaxWidth()
            )
        }
        item {
            HorizontalDivider()
            ListTitleRow(
                title = stringResource(id = R.string.recipe_steps),
                stringUiState = stepUiState,
                modifier = modifier.fillMaxWidth()
            )
        }
        itemsIndexed(stepUiState) { index, item ->
            DraggableListRow(
                label = "Steps",
                value = item,
                onValueChange = { onStepValueChange(index, it) },
                onButtonClick = { onStepRemove(item) },
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun RecipeInputForm(
    recipeDetails: RecipeDetails,
    onValueChange: (RecipeDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        placeholder = { Text(text = "Title") },
        value = recipeDetails.title,
        onValueChange = { onValueChange(recipeDetails.copy(title = it)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        modifier = modifier.padding(12.dp)
    )
    OutlinedTextField(
        placeholder = { Text(text = "Description") },
        value = recipeDetails.description,
        onValueChange = { onValueChange(recipeDetails.copy(description = it)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = modifier.padding(12.dp)
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.recipe_time),
            modifier = modifier.weight(1f)
        )
        OutlinedTextField(
            placeholder = { Text(text = "30 mins") },
            value = recipeDetails.time,
            onValueChange = { onValueChange(recipeDetails.copy(time = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = modifier.weight(2f)
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.recipe_servings),
            modifier = modifier.weight(1f)
        )
        OutlinedTextField(
            placeholder = { Text(text = "3 servings") },
            value = recipeDetails.servings,
            onValueChange = { onValueChange(recipeDetails.copy(servings = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = modifier.weight(2f)
        )
    }
}
@Composable
fun ListTitleRow(
    title: String,
    stringUiState: SnapshotStateList<String>,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        OutlinedButton(
            onClick = { stringUiState.add("") },
        ) {
            Icon(Icons.Outlined.Add, contentDescription = "Add new $title")
            Text(
                text = "Add $title",
                // textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
fun DraggableListRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(12.dp)
    ) {
        OutlinedTextField(
            placeholder = { Text(text = label) },
            value = value,
            onValueChange = {
                onValueChange(it)
                // onValueChange(recipeDetails.copy(title = it))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        IconButton(onClick = onButtonClick) {
            Icon(Icons.Default.Clear, contentDescription = null)
        }
    }
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun TestPreview() {
    RecipeListTheme {
        val ingre = remember { mutableStateListOf("雞肉切塊", "香菇", "米酒") }
        val steps = remember { mutableStateListOf("雞肉炒至上色", "加入香菇與水，小火燉煮") }
        RecipeInsertBody(
            recipeUiState = RecipeUiState(
                recipeDetails = RecipeDetails(
                    id = 1,
                    title = "title",
                    description = "description",
                    time = "95",
                    servings = "3"
                ),
            ),
            onRecipeValueChange = { },
            ingredientsUiState = ingre,
            onIngredientValueChange = { intVal, strVal -> Unit },
            onIngredientRemove = { },
            stepUiState = steps,
            onStepValueChange = { intVal, strVal -> Unit },
            onStepRemove = { },
            onSaveClicked = { },
            paddingValues = PaddingValues(12.dp)
        )
//        Column {
//            RecipeInputForm(
//                recipeDetails = RecipeDetails(
//                    id = 1,
//                    title = "title",
//                    description = "description",
//                    time = "95",
//                    servings = "3"
//                ),
//                onValueChange = { },
//                modifier = Modifier.fillMaxWidth(),
//            )
//        }
    }
}


