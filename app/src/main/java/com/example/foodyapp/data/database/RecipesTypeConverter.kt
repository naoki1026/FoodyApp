package com.example.foodyapp.data.database

import androidx.room.TypeConverter
import com.example.foodyapp.models.FoodRecipe
import com.example.foodyapp.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// JSONデータとJavaオブジェクトを相互に変換するためのライブラリ
// Gson
class RecipesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe) : String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String) : FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result) : String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String) : Result {
        // デシリアライズする場合は以下のように記述しなければいけないと覚えていく
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType)
    }
}