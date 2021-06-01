package com.example.foodyapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity : RecipesEntity)

    // コルーチンの一種で、返す値が1つのみのsuspend関数とは異なり、
    // 複数の値を順次出力することができる。例えばデータベースからリアルタイムで更新情報を
    // 受け取るような場合に使用することができる。
    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes() : Flow<List<RecipesEntity>>

}