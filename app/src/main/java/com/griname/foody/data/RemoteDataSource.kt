package com.griname.foody.data

import com.griname.foody.data.network.FoodRecipeApi
import com.griname.foody.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
){

    suspend fun getRecipes(queries: Map<String, String>) : Response<FoodRecipe> {
        return foodRecipeApi.getRecipes(queries)
    }
}