package com.example.foodyapp.data.database

import androidx.room.*

@Database(entities = [RecipesEntity::class],
    version = 1,
    exportSchema = false)

// TypeConvertersの記述が漏れていた・・・
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao() : RecipesDao
}