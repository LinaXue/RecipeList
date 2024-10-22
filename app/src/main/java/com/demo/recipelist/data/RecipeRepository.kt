package com.demo.recipelist.data

import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun insertRecipe(recipe: Recipe)

    suspend fun updateRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    fun getRecipeById(id: Int): Flow<Recipe?>

    fun getAllRecipes(): Flow<List<Recipe>>
}