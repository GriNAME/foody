package com.griname.foody.data.network

import com.griname.foody.data.network.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> =
        foodRecipeApi.getRecipes(queries)

    suspend fun searchRecipes(queries: Map<String, String>): Response<FoodRecipe> =
        foodRecipeApi.searchRecipes(queries)
}