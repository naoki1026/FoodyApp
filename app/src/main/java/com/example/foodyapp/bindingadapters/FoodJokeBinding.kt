package com.example.foodyapp.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foodyapp.data.database.entities.FoodJokeEntity
import com.example.foodyapp.models.FoodJoke
import com.example.foodyapp.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {
    companion object {
        @BindingAdapter("readApiResponse3", "readDatabase3", requireAll = false)

        // @JvmStaticが記述されてしまうとアプリがクラッシュしてしまう
        @JvmStatic
        fun setCardAndProgressVisibility(
            view : View,
            readApiResponse: NetworkResult<FoodJoke>?,
            readDatabase: List<FoodJokeEntity>?
        ){
            when(readApiResponse){
                is NetworkResult.Loading -> {
                   when (view){
                       is ProgressBar -> {
                           view.visibility = View.VISIBLE
                       }
                       is MaterialCardView -> {
                           view.visibility = View.INVISIBLE
                       }
                   }
                }
                is NetworkResult.Error -> {
                    when(view){
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if (readDatabase != null) {
                                if(readDatabase.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when(view){
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        @BindingAdapter("readApiResponse4", "readDatabase4", requireAll = false)
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View, apiResponse: NetworkResult<FoodJoke>?, database : List<FoodJokeEntity>?
        ) {
            if(!database.isNullOrEmpty()){
                view.visibility = View.VISIBLE
                if(view is TextView) {
                    if(apiResponse != null) {
                        view.text = apiResponse.message.toString()
                    }
                }
            }
            if (apiResponse is NetworkResult.Success) {
                view.visibility = View.INVISIBLE
            }

        }
    }
}