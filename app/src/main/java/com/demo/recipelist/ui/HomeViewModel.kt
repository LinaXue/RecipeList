package com.demo.recipelist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.recipelist.R
import com.demo.recipelist.data.Recipe
import com.demo.recipelist.data.RecipeRepository
import com.demo.recipelist.data.UserPreferencesRepository
import com.demo.recipelist.ui.screen.MealPlannerDestination
import com.demo.recipelist.ui.screen.PetsProfileDestination
import com.demo.recipelist.ui.screen.SettingsDestination
import com.demo.recipelist.ui.screen.UserProfileDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    recipeRepository: RecipeRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val drawerItems = listOf(
        UserProfileDestination,
        PetsProfileDestination,
        MealPlannerDestination,
        SettingsDestination
    )

    val homeUiState: StateFlow<HomeUiState> = recipeRepository.getAllRecipes().map { HomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    val layoutUiState: StateFlow<LayoutUiState> =
        userPreferencesRepository.isLinearLayout.map { isLinearLayout ->
            LayoutUiState(isLinearLayout)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = LayoutUiState()
            )

    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }
}

data class HomeUiState(val recipeList: List<Recipe> = listOf())

data class LayoutUiState(
    val isLinearLayout: Boolean = true,
    val toggleContentDescription: Int =
        if (isLinearLayout) R.string.grid_layout_toggle else R.string.linear_layout_toggle,
    val toggleIcon: Int =
        if (isLinearLayout) R.drawable.ic_grid_layout else R.drawable.ic_linear_layout
)