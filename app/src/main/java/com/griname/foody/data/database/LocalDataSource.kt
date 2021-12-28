package com.griname.foody.data.database

import com.griname.foody.data.database.entity.FavoriteEntity
import com.griname.foody.data.database.entity.FoodJokeEntity
import com.griname.foody.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao: RecipeDao
) {

    fun readRecipes(): Flow<List<RecipeEntity>>{
        return recipeDao.readRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>> {
        return recipeDao.readFavoriteRecipes()
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>> {
        return recipeDao.readFoodJoke()
    }

    suspend fun insertRecipe(recipeEntity: RecipeEntity) {
        recipeDao.insertRecipe(recipeEntity)
    }

    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        recipeDao.insertFavoriteRecipe(favoriteEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
        recipeDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        recipeDao.deleteFavoriteRecipe(favoriteEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipeDao.deleteAllFavoriteRecipes()
    }
}