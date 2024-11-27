package com.demo.recipelist.ui.screen

import androidx.lifecycle.ViewModel
import com.demo.recipelist.data.UserPreferencesRepository

data class SettingsUiState(
    val mode: Boolean,
)

class SettingsViewModel(
    userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

}