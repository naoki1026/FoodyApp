package com.example.foodyapp.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

//以下のアノテーションをつけないと画面遷移する際にエラーにななってしまう
// L73
@Parcelize
data class FoodRecipe(
    @SerializedName("results")
    val results: List<Result>
) : Parcelable