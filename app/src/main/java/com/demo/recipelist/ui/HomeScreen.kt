package com.demo.recipelist.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListApp
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.data.Recipe
import com.demo.recipelist.ui.navigation.NavigationDestination
import com.demo.recipelist.ui.screen.MealPlannerDestination
import com.demo.recipelist.ui.screen.PetsProfileDestination
import com.demo.recipelist.ui.screen.SettingsDestination
import com.demo.recipelist.ui.screen.UserProfileDestination
import com.demo.recipelist.ui.theme.RecipeListTheme
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home_screen"
    override val titleRes =  R.string.home_screen_title
    override val icon = null
}

//val items =
//    listOf(
//        Icons.Default.AccountCircle,
//        Icons.Default.Favorite,
//        Icons.Default.PushPin,
//        Icons.Default.Settings,
//    )

@Composable
fun HomeScreen(
    navigateToRecipeInsert: () -> Unit,
    navigateToRecipeDetails: (Int) -> Unit,
    navigateToUserProfile: () -> Unit,
    navigateToPetsProfile: () -> Unit,
    navigateToMealPlanner: () -> Unit,
    navigateToSettings: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val drawerItems = viewModel.drawerItems
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // val selectedItem = remember { mutableStateOf(drawerItems[0]) }
    val tag = "HomeScreen"

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(250.dp)) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Spacer(Modifier.height(12.dp))
                    drawerItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon!!, contentDescription = null) },
                            label = { Text(stringResource(id = item.titleRes)) },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                Log.d(tag, "NavigationDrawerItem: selectedItem = ${item.route}")
                                when(item) {
                                    is UserProfileDestination -> navigateToUserProfile()
                                    is PetsProfileDestination -> navigateToPetsProfile()
                                    is MealPlannerDestination -> navigateToMealPlanner()
                                    is SettingsDestination -> navigateToSettings()
                                }
                                Log.d(tag, "go to ${item.route} screen")
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                RecipeListTopAppBar(
                    title = stringResource(HomeDestination.titleRes),
                    canNavigateBack = false,
                    showModalNavigationDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    searchButton = true,
                    onSearchClick = { /*TODO*/ }
                )
            },
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
            colors = ListItemColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                headlineColor = MaterialTheme.colorScheme.primary,
                leadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                overlineColor = MaterialTheme.colorScheme.surfaceVariant,
                supportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                trailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledHeadlineColor = MaterialTheme.colorScheme.surfaceContainerLow,
                disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceContainerLow,
                disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            modifier = modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .clickable {
                onRecipeClick(recipe)
                Log.d("HomeScreen", "RecipeItem Click!")
            }
        )
    }
}

@Preview(apiLevel = 33, showBackground = false)
@Composable
fun HomeBodyPreview() {
    RecipeListTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceBright
        ) {
            HomeBody(listOf(
                Recipe(0,"butter cookies","good"),
                Recipe(1,"butter cookies123","good123"),
                Recipe(2,"butter cookies456","good456"),
                Recipe(3,"butter cookies789","good789")
            ),{}, contentPadding = PaddingValues(8.dp))
        }
    }
}