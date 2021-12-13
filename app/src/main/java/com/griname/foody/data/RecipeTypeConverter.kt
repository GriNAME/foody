package com.griname.foody.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.griname.foody.data.network.model.FoodRecipe
import com.griname.foody.data.network.model.Result

class RecipeTypeConverter {

    val gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String =
        gson.toJson(foodRecipe)

    @TypeConverter
    fun stringToFoodRecipe(foodRecipe: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(foodRecipe, listType)
    }

    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(result: String): Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(result, listType)
    }
}