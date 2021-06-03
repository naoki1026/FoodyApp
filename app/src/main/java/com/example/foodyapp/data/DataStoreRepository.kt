package com.example.foodyapp.data

// Repository
// ViewModelとModelを疎結合にするための役割（必須ではない）

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.example.foodyapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodyapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodyapp.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.foodyapp.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.foodyapp.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.foodyapp.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.foodyapp.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.foodyapp.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// クラス自体のインスタンス化、つまりコンストラクタの引数に対して注入できる
// プライマリコンストラクタの前に@Injectをつけることで引数に対して効果を発揮する
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private object PreferenceKeys {
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
        val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
    }

    // createDataStoreには2種類あるため注意
    private val dataStore : DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME
    )

    suspend fun saveMealAndDietType(mealType: String, mealTypeId : Int, dietType: String, dietTypeId : Int) {
        dataStore.edit {  preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline : Boolean){
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline

        }
    }

    val readMealAndDietType : Flow<MealAndDietType> = dataStore.data
            // 例外処理
        .catch{ exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
            // mapを使用して、データをViewに表示するための変換を行っている
        .map { preferences ->

            // 値が登録されていなかった場合は、?:より右側の値となる
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeID = preferences[PreferenceKeys.selectedDietTypeId] ?: 0

            // selectedDietTypeIDの代わりにselectedMealTypeIdが登録されており、チップに反映されていなかった
            MealAndDietType(selectedMealType, selectedMealTypeId, selectedDietType, selectedDietTypeID)
        }
    val readBackOnline : Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

// データクラスを定義
data class MealAndDietType(
    val selectedMealType : String,
    val selectedMealTypeId : Int,
    val selectedDietType : String,
    val selectedDietTypeId : Int
)