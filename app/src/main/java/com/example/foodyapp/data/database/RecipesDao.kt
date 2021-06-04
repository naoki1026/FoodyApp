package com.example.foodyapp.data.database

import androidx.room.*
import com.example.foodyapp.data.database.entities.FavoriteEntity
import com.example.foodyapp.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

// データアクセスオブジェクトの意味
// インターフェースでSQL文を記述する
@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity : RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity : FavoriteEntity)

    // コルーチンの一種で、返す値が1つのみのsuspend関数とは異なり、
    // 複数の値を順次出力することができる。例えばデータベースからリアルタイムで更新情報を
    // 受け取るような場合に使用することができる。
    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes() : Flow<List<RecipesEntity>>

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes() : Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoriteEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

}