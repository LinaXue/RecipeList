package com.demo.recipelist.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.demo.recipelist.RecipeListApplication
import com.demo.recipelist.ui.screen.PermissionViewModel
import com.demo.recipelist.ui.screen.RecipeEditViewModel
import com.demo.recipelist.ui.screen.RecipeInsertViewModel
import com.demo.recipelist.ui.screen.RecipeDetailsViewModel
import com.demo.recipelist.ui.screen.SettingsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SettingsViewModel(recipeListApplication().container.userPreferencesRepository)
        }
        initializer {
            PermissionViewModel(recipeListApplication())
        }
        initializer {
            HomeViewModel(
                recipeListApplication().container.recipeRepository,
                recipeListApplication().container.userPreferencesRepository
            )
        }
        initializer {
            RecipeInsertViewModel(recipeListApplication().container.recipeRepository)
        }
        initializer {
            RecipeDetailsViewModel(
                this.createSavedStateHandle(),
                recipeListApplication().container.recipeRepository
            )
        }
        initializer {
            RecipeEditViewModel(
                this.createSavedStateHandle(),
                recipeListApplication().container.recipeRepository
            )
        }
    }
}

fun CreationExtras.recipeListApplication(): RecipeListApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeListApplication)