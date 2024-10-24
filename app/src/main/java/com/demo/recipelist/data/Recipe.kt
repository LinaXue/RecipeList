package com.demo.recipelist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "recipe_title") var title: String,
    @ColumnInfo(name = "recipe_description") var description: String = "",
    @ColumnInfo(name = "recipe_time") var time: String = "",
    @ColumnInfo(name = "recipe_servings") var servings: Int = 1,
    @ColumnInfo(name = "recipe_ingredients") var ingredients: String = "",
    @ColumnInfo(name = "recipe_steps") var steps: String = ""
)
