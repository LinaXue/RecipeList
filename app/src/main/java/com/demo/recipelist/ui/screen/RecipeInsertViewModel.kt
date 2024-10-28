package com.demo.recipelist.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    fun validateInput(
        uiState: RecipeDetails = recipeUiState.recipeDetails
    ): Boolean {
        return with(uiState) {
            title.isNotBlank() && time.isNotBlank() && servings.isNotBlank()
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
        if (recipeUiState.recipeDetails.title.isNotBlank()) {
            val newRecipe = recipeUiState.recipeDetails.toRecipe()
            newRecipe.ingredients = validateIngredients()
            newRecipe.steps = validateSteps()
            recipeRepository.insertRecipe(newRecipe)
        }
    }
}

data class RecipeDetails(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val servings: String = "",
    val ingredients: String = "",
    val steps: String = ""
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
    servings = servings.toIntOrNull() ?: 1,
    ingredients = ingredients,
    steps = steps
)

fun Recipe.toRecipeDetail(): RecipeDetails = RecipeDetails(
    id = id,
    title = title,
    description = description,
    time = time,
    servings = servings.toString(),
    ingredients = ingredients,
    steps = steps
)

fun Recipe.toRecipeUiState(isInputValid: Boolean = false): RecipeUiState = RecipeUiState(
    recipeDetails = this.toRecipeDetail(),
    isInputValid = isInputValid
)
