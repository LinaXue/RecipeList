package com.demo.recipelist.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.data.Recipe
import com.demo.recipelist.ui.navigation.NavigationDestination
import com.demo.recipelist.ui.theme.RecipeListTheme

object HomeDestination : NavigationDestination {
    override val route = "home_screen"
    override val titleRes =  R.string.home_screen_title
}

@Composable
fun HomeScreen(
    navigateToRecipeInsert: () -> Unit,
    navigateToRecipeDetails: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            RecipeListTopAppBar(
            title = stringResource(HomeDestination.titleRes),
            canNavigateBack = false
        ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToRecipeInsert() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add the recipe.")
            }
        }
    ) {
        HomeBody(
            recipeList = homeUiState.recipeList,
            onRecipeClick = navigateToRecipeDetails,
            contentPadding = it
        )
    }
}

@Composable
fun HomeBody(
    recipeList: List<Recipe>,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (recipeList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            )
        } else {
            RecipeList(
                recipeList = recipeList,
                onRecipeClick = { onRecipeClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun RecipeList(
    recipeList: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        items(items = recipeList, key = {it.id}) {recipe ->
            RecipeItem(
                recipe = recipe,
                onRecipeClick = { onRecipeClick(recipe) },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onRecipeClick: (Recipe) -> Unit, modifier: Modifier = Modifier) {
    Column {
        ListItem(
            headlineContent = { Text(text = recipe.title) },
            supportingContent = { Text(text = recipe.description) },
            modifier = modifier.clickable {
                onRecipeClick(recipe)
                Log.d("HomeScreen", "RecipeItem Click!")
            }
        )
    }
    HorizontalDivider()
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun HomeScreenPreview() {
    RecipeListTheme {
        HomeScreen({},{} )
    }
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun HomeBodyPreview() {
    RecipeListTheme {
        HomeBody(listOf(
            Recipe(0,"butter cookies","good"),
            Recipe(1,"butter cookies123","good123"),
            Recipe(2,"butter cookies456","good456"),
            Recipe(3,"butter cookies789","good789")
        ),{}, contentPadding = PaddingValues(8.dp))
        // HomeView()
    }
}


