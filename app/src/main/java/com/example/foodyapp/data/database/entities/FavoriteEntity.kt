package com.example.foodyapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodyapp.models.Result
import com.example.foodyapp.util.Constants


// テーブルクラス
// Entityは実体という意味
@Entity(tableName = Constants.FAVORITE_RECIPES_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var result : Result
){

}