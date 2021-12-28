package com.griname.foody.data.network

import com.griname.foody.data.network.model.FoodJoke
import com.griname.foody.data.network.model.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String, String>): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(@QueryMap searchQueries: Map<String, String>): Response<FoodRecipe>

    @GET("/food/jokes/random")
    suspend fun getFoodJoke(@Query("apiKey") apiKey: String): Response<FoodJoke>
}