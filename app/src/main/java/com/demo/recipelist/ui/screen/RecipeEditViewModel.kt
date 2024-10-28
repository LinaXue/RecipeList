package com.demo.recipelist.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.recipelist.data.RecipeRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecipeEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    var recipeUiState by mutableStateOf(RecipeUiState())
        private set
    var ingredientUiState = mutableStateListOf("")
        private set
    var stepUiState = mutableStateListOf("")
        private set

    private val recipeId: Int = checkNotNull(savedStateHandle[EditDestination.recipeIdArg])

    init {
        viewModelScope.launch {
            recipeUiState = recipeRepository.getRecipeById(recipeId)
                .filterNotNull()
                .first()
                .toRecipeUiState(isInputValid = true)
            ingredientUiState = stringToList(recipeUiState.recipeDetails.ingredients)
            stepUiState = stringToList(recipeUiState.recipeDetails.steps)
        }
    }
    private fun stringToList(items: String, pattern: String = "/nexTNext/"): SnapshotStateList<String> {
        val newList = items.split(regex = pattern.toPattern()).toMutableList()
        newList.removeAt(newList.size - 1)
        return mutableStateListOf<String>().apply {
            addAll(newList)
        }
    }
    fun updateUiState(recipeDetails: RecipeDetails) {
        recipeUiState =
            RecipeUiState(recipeDetails = recipeDetails, isInputValid = validateInput(recipeDetails))
    }
    fun updateIngredientUiState(index: Int, newIngredientUiState: String) {
        ingredientUiState[index] = newIngredientUiState
    }
    fun deleteIngredientUiState(ingredient: String) {
        ingredientUiState.remove(ingredient)
    }
    fun updateStepUiState(index: Int, newStepUiState: String) {
        stepUiState[index] = newStepUiState
    }
    fun deleteStepUiState(step: String) {
        stepUiState.remove(step)
    }

    suspend fun updateRecipe() {
        if (validateInput(recipeUiState.recipeDetails)) {
            val newRecipe = recipeUiState.recipeDetails.toRecipe()
            newRecipe.ingredients = validateIngredients()
            newRecipe.steps = validateSteps()
            recipeRepository.updateRecipe(newRecipe)
        }
    }

    fun validateInput(uiState: RecipeDetails = recipeUiState.recipeDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank() && time.isNotBlank()
        }
    }
    private fun validateIngredients(pattern: String = "/nexTNext/"): String {
        var ingredientsString = ""
        for (ing in ingredientUiState) {
            if (ing.isNotBlank()) {
                ingredientsString = ingredientsString + ing + pattern
            }
        }
        return ingredientsString
    }
    private fun validateSteps(pattern: String = "/nexTNext/"): String {
        var stepsString = ""
        for (step in stepUiState) {
            if (step.isNotBlank()) {
                stepsString = stepsString + step + pattern
            }
        }
        return stepsString
    }
}