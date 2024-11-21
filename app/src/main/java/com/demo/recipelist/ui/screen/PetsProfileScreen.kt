package com.demo.recipelist.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.ui.navigation.NavigationDestination

object PetsProfileDestination: NavigationDestination {
    override val route = "pets_profile_screen"
    override val titleRes = R.string.pets_profile_screen_title
    override val icon = Icons.Default.Pets
}
@Composable
fun PetsProfileScreen(
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
) {
    Scaffold(
        topBar = {
            RecipeListTopAppBar(
                title = stringResource(PetsProfileDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
            )
        },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it).fillMaxSize()
        ) {
            Text(text = "Pets Profile Screen")
        }
    }
}