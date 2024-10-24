package com.demo.recipelist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Recipe::class], version = 2, exportSchema = false)
abstract class RecipeDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var Instance: RecipeDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE recipes ADD COLUMN 'recipe_ingredients' TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE recipes ADD COLUMN 'recipe_steps' TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): RecipeDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RecipeDatabase::class.java, "recipe_database")
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}