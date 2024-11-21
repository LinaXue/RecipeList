package com.demo.recipelist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.demo.recipelist.ui.navigation.RecipeListNavHost
import com.demo.recipelist.ui.theme.RecipeListTheme

@Composable
fun RecipeListApp(navController: NavHostController = rememberNavController()) {
    RecipeListNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListTopAppBar(
    title: String,
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    showModalNavigationDrawer: () -> Unit = {},
    saveButton: Boolean = false,
    saveEnable: Boolean = false,
    onSaveClick: () -> Unit = {},
    editButton: Boolean = false,
    onEditClick: () -> Unit = {},
    deleteButton: Boolean = false,
    onDeleteClick: () -> Unit = {},
    searchButton: Boolean = false,
    onSearchClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                IconButton(onClick = showModalNavigationDrawer) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "List"
                    )
                }
            }
        },
        actions = {
            if (searchButton) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
//            IconButton(
//                onClick = { selectLayout(!isLinearLayout) }
//            ) {
//                Icon(
//                    painter = painterResource(uiState.toggleIcon),
//                    contentDescription = stringResource(uiState.toggleContentDescription),
//                    tint = MaterialTheme.colorScheme.onBackground
//                )
//            }
            if (saveButton) {
                IconButton(
                    onClick = onSaveClick,
                    enabled = saveEnable,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Save"
                    )
                }
            }
            if (editButton) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
            if (deleteButton) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        },

    )
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun RecipeListTopAppBarPreview() {
    RecipeListTheme {
        RecipeListTopAppBar(
            title = "TEST Screen",
            canNavigateBack = false,
            editButton = true,
            deleteButton = true,
        )
    }
}