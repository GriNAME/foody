package com.griname.foody.util

class Constant {

    companion object {
        // Url
        const val API_KEY = "29b507ab38284b3db4e040ae27fd7623"
        const val BASE_URL = "https://api.spoonacular.com"

        // API Query Keys
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM
        const val DATABASE_NAME = "recipe_database"
        const val RECIPE_TABLE = "recipe_table"

        // Bottom Sheet and Preferences
        const val DEFAULT_RECIPE_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val DEFAULT_TYPE_ID = 0

        const val PREFERENCES_NAME = "data_store_name"
        const val PREFERENCES_MEAL_TYPE = "meal_type"
        const val PREFERENCES_MEAL_TYPE_ID = "meal_type_id"
        const val PREFERENCES_DIET_TYPE = "diet_type"
        const val PREFERENCES_DIET_TYPE_ID = "diet_type_id"
        const val PREFERENCES_BACK_ONLINE = "back_online"

        // Keys
        const val BUNDLE_RECIPE_ARGS = "recipe_bundle"
    }
}