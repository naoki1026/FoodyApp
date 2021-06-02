package com.example.foodyapp.util

class Constants {

    // シングルトンで外部のクラスにより変更することができない唯一の存在のこと
    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "85103093f86b4ebfbfda85266745cf78"
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
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"

    }
}