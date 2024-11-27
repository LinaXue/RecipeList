package com.demo.recipelist.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.recipelist.R
import com.demo.recipelist.data.UserPreferencesRepository
import com.demo.recipelist.ui.LayoutUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val mode: Boolean = false,
)

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        userPreferencesRepository.isDarkTheme.map { isDarkTheme ->
            SettingsUiState(isDarkTheme)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState()
            )

    fun switchTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveThemePreference(isDarkTheme)
        }
    }
}