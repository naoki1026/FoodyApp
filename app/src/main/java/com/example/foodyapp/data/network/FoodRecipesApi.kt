package com.example.foodyapp.data.network

import com.example.foodyapp.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {
    @GET("/recipes/complexSearch")
    // suspendを使用して非同期通信処理を行っている
    suspend fun getRecipes(
        @QueryMap queries : Map<String, String>
    ) : Response<FoodRecipe>
}