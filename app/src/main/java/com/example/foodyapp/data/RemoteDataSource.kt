package com.example.foodyapp.data

import com.example.foodyapp.data.network.FoodRecipesApi
import com.example.foodyapp.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

// クラス自体のインスタンス化、つまりコンストラクタの引数に対して注入できる
// プライマリコンストラクタの前に@Injectをつけることで引数に対して効果を発揮する
//
class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>) : Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    // search Recipes
    suspend fun searchRecipes(searchQuery : Map<String, String>) : Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }
}