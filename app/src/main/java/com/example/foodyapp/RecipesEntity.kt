package com.example.foodyapp

import android.content.Context
import androidx.room.*
import com.example.foodyapp.models.FoodRecipe
import com.example.foodyapp.util.Constants.Companion.DATABASE_NAME
import com.example.foodyapp.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity (
    var foodRecipe : FoodRecipe
){
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0

}