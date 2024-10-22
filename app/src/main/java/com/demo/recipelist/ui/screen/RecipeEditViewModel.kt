package com.demo.recipelist.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.recipelist.data.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    var recipeUiState by mutableStateOf(RecipeUiState())
        private set

    private val recipeId: Int = checkNotNull(savedStateHandle[EditDestination.recipeIdArg])

    init {
        viewModelScope.launch {
            recipeUiState = recipeRepository.getRecipeById(recipeId)
                .filterNotNull()
                .first()
                .toRecipeUiState(isInputValid = true)
        }
    }

    fun updateUiState(recipeDetails: RecipeDetails) {
        recipeUiState =
            RecipeUiState(recipeDetails = recipeDetails, isInputValid = validateInput(recipeDetails))
    }

    suspend fun updateRecipe() {
        if (validateInput(recipeUiState.recipeDetails)) {
            recipeRepository.updateRecipe(recipeUiState.recipeDetails.toRecipe())
        }
    }

    private fun validateInput(uiState: RecipeDetails = recipeUiState.recipeDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank() && time.isNotBlank()
        }
    }
}