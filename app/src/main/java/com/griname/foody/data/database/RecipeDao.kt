package com.griname.foody.data.database

import androidx.room.*
import com.griname.foody.data.database.entity.FavoriteEntity
import com.griname.foody.data.database.entity.FoodJokeEntity
import com.griname.foody.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM recipe_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM favorite_recipe_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

    @Query("DELETE FROM favorite_recipe_table")
    suspend fun deleteAllFavoriteRecipes()

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity)
}