package com.example.foodyapp

import android.content.Context
import androidx.room.*

@Database(entities = [RecipesEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao() : RecipesDao
}