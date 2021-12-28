package com.griname.foody.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.griname.foody.R
import com.griname.foody.data.network.model.FoodRecipe
import com.griname.foody.data.Repository
import com.griname.foody.data.database.entity.FavoriteEntity
import com.griname.foody.data.database.entity.FoodJokeEntity
import com.griname.foody.data.database.entity.RecipeEntity
import com.griname.foody.data.network.model.FoodJoke
import com.griname.foody.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
) : AndroidViewModel(application) {

    val context = application

    /** ROOM DATABASE */
    val readRecipes: LiveData<List<RecipeEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoriteEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipe(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipe(recipeEntity)
    }

    fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertFavoriteRecipe(favoriteEntity)
    }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertFoodJoke(foodJokeEntity)
    }

    fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteFavoriteRecipe(favoriteEntity)
    }

    fun deleteAllFavoriteRecipes() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavoriteRecipes()
    }

    /** RETROFIT */
    var recipeResponse = MutableLiveData<NetworkResult<FoodRecipe>>()
    var searchRecipesResponse = MutableLiveData<NetworkResult<FoodRecipe>>()
    var foodJokeResponse = MutableLiveData<NetworkResult<FoodJoke>>()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeColl(queries)
    }

    fun searchRecipes(searchQueries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQueries)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }

    private suspend fun getRecipesSafeColl(queries: Map<String, String>) {

        recipeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipeResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipeResponse.value!!.data
                if (foodRecipe != null)
                    offlineCacheRecipe(foodRecipe)
            } catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error(context.getString(R.string.recipe_not_found))
            }
        } else {
            recipeResponse.value = NetworkResult.Error(context.getString(R.string.no_internet_connection))
        }
    }

    private suspend fun searchRecipesSafeCall(searchQueries: Map<String, String>) {

        searchRecipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQueries)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchRecipesResponse.value = NetworkResult.Error(context.getString(R.string.recipe_not_found))
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error(context.getString(R.string.no_internet_connection))
        }
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data

                if (foodJoke != null)
                    offlineCacheFoodJoke(foodJoke)

            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error(context.getString(R.string.recipe_not_found))
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error(context.getString(R.string.no_internet_connection))
        }
    }

    private fun offlineCacheRecipe(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipeEntity(foodRecipe)
        insertRecipe(recipeEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {

        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error(context.getString(R.string.api_key_limited))
            }
            response.body()?.results.isNullOrEmpty() -> {
                NetworkResult.Error(context.getString(R.string.response_is_not_found))
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> NetworkResult.Error(response.message())
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {

        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error(context.getString(R.string.api_key_limited))
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> NetworkResult.Error(response.message())
        }
    }

    fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activityNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}