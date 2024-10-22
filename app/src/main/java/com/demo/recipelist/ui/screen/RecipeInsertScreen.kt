package com.demo.recipelist.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.data.Steps
import com.demo.recipelist.ui.AppViewModelProvider
import com.demo.recipelist.ui.navigation.NavigationDestination
import com.demo.recipelist.ui.theme.RecipeListTheme
import kotlinx.coroutines.launch
import java.util.Collections

object InsertDestination : NavigationDestination {
    override val route = "insert_screen"
    override val titleRes =  R.string.insert_screen_title
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
        ) },
    ) {
        RecipeInsertBodyTest(
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
//        RecipeInsertBody(
//            recipeUiState = viewModel.recipeUiState,
//            onRecipeValueChange = viewModel::updateRecipeUiState,
//            onSaveClicked = {
//                coroutineScope.launch {
//                    viewModel.insertRecipe()
//                    navigateBack()
//                }
//            },
//            paddingValues = it
//        )
    }
}

@Composable
fun RecipeInsertBody(
    recipeUiState: RecipeUiState,
    onRecipeValueChange: (RecipeDetails) -> Unit,
    onSaveClicked: () -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(paddingValues)
            //.verticalScroll(rememberScrollState())
    ) {
        RecipeInputForm(
            recipeDetails = recipeUiState.recipeDetails,
            onValueChange = onRecipeValueChange,
        )
        Spacer(modifier = modifier.height(16.dp))
        // DraggableList("食材")
        Spacer(modifier = modifier.height(16.dp))
        Button(
            onClick = onSaveClicked,
            enabled = recipeUiState.isInputValid,
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = stringResource(id = R.string.save_button))
        }
    }
}

@Composable
fun RecipeInsertBodyTest(
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
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.recipe_ingredients),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(horizontal = 12.dp))
        }
        itemsIndexed(ingredientsUiState) { index, item ->
            DraggableListRow(
                label = "Ingredient",
                value = item,
                onValueChange = {
                    onIngredientValueChange(index, it)
                    Log.d("InsertScreen", "Ingredients List Index: $index , item: $item, change: $it")
                },
                onButtonClick = { onIngredientRemove(item) })
        }
        item {
            // Button to add new item
            Button(
                onClick = {
                    ingredientsUiState.add("")
                    Log.d("InsertScreen", "Ingredient List Item size: ${ingredientsUiState.size}")
                }
            ) {
                Text(
                    text = stringResource(id = R.string.add_ingredient_button),
                    textAlign = TextAlign.Center)
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.recipe_steps),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(horizontal = 12.dp))
        }
        itemsIndexed(stepUiState) { index, item ->
            DraggableListRow(
                label = "Steps",
                value = item,
                onValueChange = {
                    onStepValueChange(index, it)
                    Log.d("InsertScreen", "Steps List Index: $index , item: $item, change: $it")
                },
                onButtonClick = { onStepRemove(item) } )
        }
        item {
            // Button to add new item
            Button(
                onClick = {
                    stepUiState.add("")
                    Log.d("InsertScreen", "Steps List Item size: ${stepUiState.size}")
                }
            ) {
                Text(
                    text = stringResource(id = R.string.add_step_button),
                    textAlign = TextAlign.Center)
            }
        }
        item {
            Button(
                onClick = onSaveClicked,
                enabled = recipeUiState.isInputValid,
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = stringResource(id = R.string.save_button))
            }
        }
    }
}

@Composable
fun RecipeInputForm(
    recipeDetails: RecipeDetails,
    onValueChange: (RecipeDetails) -> Unit,
    modifier: Modifier = Modifier,
){
    Spacer(modifier = modifier.height(5.dp))
    OutlinedTextField(
        label = { Text(text = "title") },
        value = recipeDetails.title,
        onValueChange = { onValueChange(recipeDetails.copy(title = it)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    )
    Spacer(modifier = modifier.height(5.dp))
    OutlinedTextField(
        label = { Text(text = "Description") },
        value = recipeDetails.description,
        onValueChange = { onValueChange(recipeDetails.copy(description = it)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    )
    Spacer(modifier = modifier.height(5.dp))
    OutlinedTextField(
        label = { Text(text = "Time") },
        value = recipeDetails.time,
        onValueChange = { onValueChange(recipeDetails.copy(time = it)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    )
    Spacer(modifier = modifier.height(5.dp))
    OutlinedTextField(
        label = { Text(text = "Servings") },
        value = recipeDetails.servings,
        onValueChange = { onValueChange(recipeDetails.copy(servings = it)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    )
    Spacer(modifier = modifier.height(10.dp))
}

@Composable
fun DraggableRow(
    item: String,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    onDrop: (Int) -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(if (isDragging) Color.LightGray else Color.White)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                        onDragStart()
                    },
                    onDragEnd = {
                        isDragging = false
                        onDragEnd()
                    },
                    onDrag = { change, dragAmount ->
                        // handle dragging
                        if (dragAmount.y != 0f) {
                            // detect drag end position
                            val targetIndex = (change.position.y / 60.dp.toPx()).toInt()
                            onDrop(targetIndex)
                        }
                    }
                )
            }
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = item)
    }
}

@Composable
fun DraggableListRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier) {
//    ListItem(
//        headlineContent = { Text(text = item) },
//        modifier = modifier
//            .padding(horizontal = 10.dp)
//            .fillMaxWidth()
//    )
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(12.dp)) {
        OutlinedTextField(
            label = { Text(text = label) },
            value = value,
            onValueChange = {
                onValueChange(it)
                Log.d("InsertScreen", "DraggableListRow onValueChange!")
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
    DraggableListRow(
        label = "label",
        value = "item",
        onValueChange = {},
        onButtonClick = {}
    )
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun ItemDetailViewPreview() {
    RecipeListTheme {
            RecipeInsertBody(
                recipeUiState = RecipeUiState(
                    recipeDetails = RecipeDetails(
                        id = 0,
                        title = "標題",
                        description = "內容",
                    )
                ),
                onRecipeValueChange = {},
                onSaveClicked = {},
                paddingValues = PaddingValues(12.dp)
            )

//        Column(modifier = Modifier.padding(14.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,) {
//            RecipeInputForm(
//                recipeDetails = RecipeDetails(
//                    id = 0,
//                    title = "食譜標題",
//                    description = "步驟描述",
//                    time = "30",
//                    servings = "1"
//                ),
//                onValueChange = { },
//                modifier = Modifier.padding(10.dp)
//            )
//        }
    }
}

