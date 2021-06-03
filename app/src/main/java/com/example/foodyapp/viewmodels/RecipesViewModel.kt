package com.example.foodyapp.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodyapp.data.DataStoreRepository
import com.example.foodyapp.util.Constants
import com.example.foodyapp.util.Constants.Companion.API_KEY
import com.example.foodyapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodyapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodyapp.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foodyapp.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foodyapp.util.Constants.Companion.QUERY_API_KET
import com.example.foodyapp.util.Constants.Companion.QUERY_DIET
import com.example.foodyapp.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.foodyapp.util.Constants.Companion.QUERY_NUMBER
import com.example.foodyapp.util.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// ViewModel
// ModelとViewの間を取り持ち、データを管理する部分

// @ViewModelInject constructorとすることでViewModelをInjectすることができる
class RecipesViewModel @ViewModelInject constructor
    (application : Application,
     private val dataStoreRepository: DataStoreRepository)
    : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int)

    // Dispatcher.IOは、メインスレッドの外部でディスクまたはネットワークのI/Oを実行する場合に適しており、
    // Roomコンポーネントの使用、ファイルの読み書き、ネットワークオペレーションの実行等である
    = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
    }

    fun applyQueries(): HashMap<String, String>{
        val queries : HashMap<String, String> = HashMap()

        viewModelScope.launch {
            // Flow.collectで値を受け取っていく
            readMealAndDietType.collect {  value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType

                // OK
                println("applyQueries mealType : ${mealType}, dietType : ${dietType}")
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KET] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        println("After readMealAndDietType queries : ${queries}")
        return queries
    }
}