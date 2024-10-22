package com.demo.recipelist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT * from recipes WHERE id = :id")
    fun getRecipeById(id: Int): Flow<Recipe>

//    @Query("SELECT * from recipes WHERE recipe_title = :title")
//    fun getRecipesByName(title: String): Flow<List<Recipe>>

    @Query("SELECT * from recipes ORDER BY recipe_title ASC")
    fun getAllRecipes(): Flow<List<Recipe>>
}