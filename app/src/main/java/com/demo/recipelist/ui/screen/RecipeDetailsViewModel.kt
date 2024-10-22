package com.demo.recipelist.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.recipelist.data.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RecipeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
): ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val recipeId: Int = checkNotNull(savedStateHandle[DetailsDestination.recipeIdArg])

    val uiState: StateFlow<RecipeDetails> =
        recipeRepository.getRecipeById(recipeId)
            .filterNotNull()
            .map {
                it.toRecipeDetail()
                // ItemDetailsUiState(itemDetails = it.toItemDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = RecipeDetails()
            )

    suspend fun deleteItem() {
        recipeRepository.deleteRecipe(uiState.value.toRecipe())
    }
}
