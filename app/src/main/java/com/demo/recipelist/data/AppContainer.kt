package com.demo.recipelist.data

import android.content.Context

interface AppContainer {
    val recipeRepository: RecipeRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val recipeRepository: RecipeRepository by lazy {
        OfflineRecipeRepository(RecipeDatabase.getDatabase(context).recipeDao())
    }
}