package com.demo.recipelist.ui.screen

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.ui.navigation.NavigationDestination

object SettingsDestination : NavigationDestination {
    override val route = "settings_screen"
    override val titleRes = R.string.settings_screen_title
    override val icon = Icons.Default.Settings
}

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
) {
    Scaffold(
        topBar = {
            RecipeListTopAppBar(
                title = stringResource(SettingsDestination.titleRes),
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
            Text(text = "Test Screen")
        }
    }
}