package com.example.foodyapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foodyapp.data.Repository
import com.example.foodyapp.data.database.RecipesEntity
import com.example.foodyapp.models.FoodRecipe
import com.example.foodyapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import java.lang.Exception

// ViewModel
// ModelとViewの間を取り持ち、データを管理する部分

class MainViewModel @ViewModelInject constructor(
    private val repository : Repository,
    application: Application
) : AndroidViewModel(application){


    /** ROOM DATABASE */

    // Flowでは、asLiveDataを使うことで、LiveDataに変換することができる
    val readRecipes : LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()
    private fun insertRecipes(recipesEntity: RecipesEntity) =

   // Dispatcher.IOは、メインスレッドの外部でディスクまたはネットワークのI/Oを実行する場合に適しており、
        // Roomコンポーネントの使用、ファイルの読み書き、ネットワークオペレーションの実行等である
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    /** RETROFIT */
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        println("getRecipes : ${queries}")
        getRecipesSafeCall(queries)
    }
    
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        if (hasInternetConnection()) {
            println("hasInternetConnection() : ${hasInternetConnection()}")
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

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>) : NetworkResult<FoodRecipe>?{
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
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

    private fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}