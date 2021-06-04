package com.example.foodyapp.data.database

import androidx.room.*
import com.example.foodyapp.data.database.entities.RecipesEntity

// 抽象クラス
// データベース全体の内容を記述する
@Database(entities = [RecipesEntity::class],
    version = 1,
    exportSchema = false)

// TypeConvertersの記述が漏れていた・・・
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao() : RecipesDao
}