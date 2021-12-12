package com.griname.foody.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.griname.foody.R
import com.griname.foody.data.network.model.FoodRecipe
import com.griname.foody.data.Repository
import com.griname.foody.data.database.entity.RecipeEntity
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
    private val repository: Repository
) : AndroidViewModel(application) {

    val context = application

    /** ROOM DATABASE */
    val readRecipes: LiveData<List<RecipeEntity>> = repository.localDataSource.readRecipes().asLiveData()

    fun insertRecipe(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.localDataSource.recipeInsert(recipeEntity)
    }

    /** RETROFIT */
    var recipeResponse = MutableLiveData<NetworkResult<FoodRecipe>>()
    var searchRecipesResponse = MutableLiveData<NetworkResult<FoodRecipe>>()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeColl(queries)
    }

    fun searchRecipes(searchQueries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQueries)
    }

    private suspend fun getRecipesSafeColl(queries: Map<String, String>) {

        recipeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remoteDataSource.getRecipes(queries)
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
                val response = repository.remoteDataSource.searchRecipes(searchQueries)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchRecipesResponse.value = NetworkResult.Error(context.getString(R.string.recipe_not_found))
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error(context.getString(R.string.no_internet_connection))
        }
    }

    private fun offlineCacheRecipe(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipeEntity(foodRecipe)
        insertRecipe(recipeEntity)
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