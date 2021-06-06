package com.example.foodyapp.data.network

import com.example.foodyapp.models.FoodJoke
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

    // search Recipes
    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ) : Response<FoodRecipe>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(

        // エンドポイント（終点、終末点）が1つしかないため、@QueryMAPではなく@Queryを使用する
        @Query ("apiKey") apiKey : String
    ) : Response<FoodJoke>
}