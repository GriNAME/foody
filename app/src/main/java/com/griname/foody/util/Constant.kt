package com.griname.foody.util

class Constant {

    companion object {
        const val API_KEY = "29b507ab38284b3db4e040ae27fd7623"
        const val BASE_URL = "https://api.spoonacular.com"

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM
        const val DATABASE_NAME = "recipe_database"
        const val RECIPE_TABLE = "recipe_table"
    }
}