package com.demo.recipelist.data

import kotlinx.coroutines.flow.Flow

class OfflineRecipeRepository(private val recipeDao: RecipeDao): RecipeRepository {
    override suspend fun insertRecipe(recipe: Recipe) = recipeDao.insertRecipe(recipe)

    override suspend fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)

    override suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)

    override fun getRecipeById(id: Int): Flow<Recipe?> = recipeDao.getRecipeById(id)

    override fun getAllRecipes(): Flow<List<Recipe>> = recipeDao.getAllRecipes()
}