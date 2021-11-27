package com.griname.foody.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.griname.foody.model.FoodRecipe

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
}