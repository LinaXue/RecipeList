package com.demo.recipelist.ui.screen

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.ui.AppViewModelProvider
import com.demo.recipelist.ui.navigation.NavigationDestination
import com.demo.recipelist.ui.theme.RecipeListTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

object SettingsDestination : NavigationDestination {
    override val route = "settings_screen"
    override val titleRes = R.string.settings_screen_title
    override val icon = Icons.Default.Settings
}

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val sittingsUiState = viewModel.settingsUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            RecipeListTopAppBar(
                title = stringResource(SettingsDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
            )
        },
    ) {
        SettingsBody(
            sittingsUiState = sittingsUiState.value,
            switchTheme = { mode ->
                coroutineScope.launch {

                    viewModel.switchTheme(mode)
                    Log.d("switchTheme","current theme is $mode")
                }
            },
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun SettingsBody(
    sittingsUiState: SettingsUiState,
    switchTheme: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "Test Screen")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Text(text = if (sittingsUiState.mode) "Dark Theme" else "Light Theme")
            Switch(checked = sittingsUiState.mode, onCheckedChange = { switchTheme(it) })
        }
    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
fun SettingsScreenPreview() {
        SettingsBody(SettingsUiState(false),{})
}