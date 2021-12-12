package com.griname.foody.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.griname.foody.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipe_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipeEntity>>
}