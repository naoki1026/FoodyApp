package com.example.foodyapp.models


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class FoodJoke(
    @SerializedName("text")
    val text: String
)