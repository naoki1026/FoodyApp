package com.example.foodyapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foodyapp.data.Repository
import com.example.foodyapp.data.database.entities.FavoriteEntity
import com.example.foodyapp.data.database.entities.RecipesEntity
import com.example.foodyapp.models.FoodRecipe
import com.example.foodyapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

// ViewModel
// ModelとViewの間を取り持ち、データを管理する部分

class MainViewModel @ViewModelInject constructor(
    private val repository : Repository,
    application: Application
) : AndroidViewModel(application){


    /** ROOM DATABASE */
    /** Recipe */

    // Flowでは、asLiveDataを使うことで、LiveDataに変換することができる
    val readRecipes : LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
   // Dispatcher.IOは、メインスレッドの外部でディスクまたはネットワークのI/Oを実行する場合に適しており、
        // Roomコンポーネントの使用、ファイルの読み書き、ネットワークオペレーションの実行等である
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    /** Favorite */
    val readFavoriteEntity : LiveData<List<FavoriteEntity>> = repository.local.readFavoriteRecipes().asLiveData()

     fun insertFavorites(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoriteEntity)
        }

     fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipes(favoriteEntity)
        }

     fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }

    /** RETROFIT */
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchedRecipesSafeCall(searchQuery)
    }

    private suspend fun searchedRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)

            } catch (e: Exception) {
                searchedRecipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            searchedRecipesResponse.value = NetworkResult.Error("No Internet connection.")
        }
    }

    // インターネットの接続状況をチェックして、取得できた場合はオフラインキャッシュを行う
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

               // 取得してきたデータをオフラインキャッシュする
                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }

            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recupes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet connection.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    // レスポンスの状態をチェックする
    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>) : NetworkResult<FoodRecipe>?{
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            // 402　PaymentRequired クライアントが支払いを行っていないため、
            // APIの取得ができない状態
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    // インターネットヘの接続状況をチェックする
    private fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {

            // WiFiだった場合
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // 携帯通信だった場合
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // 有線ネットワークだった場合
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}