package com.example.foodyapp.util

class Constants {

    // シングルトンで外部のクラスにより変更することができない唯一の存在のこと
    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "a5889925b67e47dc8735a7689c3196e8"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KET = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM DATABASE
        const val DATABASE_NAME = "recipe_database"
        const val RECIPES_TABLE = "recipes_table"

        // Bottom Sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "50"

        // DEFAULT_MEAL_TYPEとDEFAULT_DIET_TYPEが変わらない
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"

    }
}