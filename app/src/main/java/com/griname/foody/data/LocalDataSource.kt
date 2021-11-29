package com.griname.foody.data

import com.griname.foody.data.database.RecipeDao
import com.griname.foody.data.database.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao: RecipeDao
) {

    fun readRecipes(): Flow<List<RecipeEntity>>{
        return recipeDao.readRecipes()
    }

    suspend fun recipeInsert(recipeEntity: RecipeEntity) {
        recipeDao.insertRecipe(recipeEntity)
    }
}