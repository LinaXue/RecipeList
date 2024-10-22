package com.demo.recipelist.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.demo.recipelist.data.Recipe
import com.demo.recipelist.data.RecipeRepository

class RecipeInsertViewModel(
    private val recipeRepository: RecipeRepository
): ViewModel() {

    var recipeUiState by mutableStateOf(RecipeUiState())
        private set

    var ingredientUiState = mutableStateListOf("", "")
        private set

    var stepUiState = mutableStateListOf("", "")
        private set

    private fun validateInput(uiState: RecipeDetails = recipeUiState.recipeDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank() && time.isNotBlank()
        }
    }
    fun updateRecipeUiState(recipeDetails: RecipeDetails) {
        recipeUiState = RecipeUiState(recipeDetails = recipeDetails, isInputValid = validateInput(recipeDetails))
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
    suspend fun insertRecipe() {
        if (validateInput()) {
            recipeRepository.insertRecipe(recipeUiState.recipeDetails.toRecipe())
        }
    }
}

data class RecipeDetails(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val time: String = "30",
    val servings: String = "1"
)

data class RecipeUiState(
    val recipeDetails: RecipeDetails = RecipeDetails(),
    val isInputValid: Boolean = false
)

fun RecipeDetails.toRecipe(): Recipe = Recipe(
    id = id,
    title = title,
    description = description,
    time = time,
    servings = servings.toIntOrNull() ?: 1
)

fun Recipe.toRecipeDetail(): RecipeDetails = RecipeDetails(
    id = id,
    title = title,
    description = description,
    time = time,
    servings = servings.toString()
)

fun Recipe.toRecipeUiState(isInputValid: Boolean = false): RecipeUiState = RecipeUiState(
    recipeDetails = this.toRecipeDetail(),
    isInputValid = isInputValid
)
