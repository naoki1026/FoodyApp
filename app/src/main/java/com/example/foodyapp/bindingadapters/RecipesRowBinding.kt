package com.example.foodyapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

import coil.load
import com.example.foodyapp.R

class RecipesRowBinding {

    // クラスの中にシングルトンを定義することでインスタンスなしでメソッドを呼び出すことができる
    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String){
            imageView.load(imageUrl) {
                crossfade(600)

                // 画像データを取得できなかった場合に代わりの画像を表示させる
                error(R.drawable.ic_error_placeholder)
            }
        }

        // データバインディングでData型を渡された時にフォーマットして表示する際に、
        //　以下2種類のアノテーションを定義する必要がある
        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean){
            if(vegan) {

                // viewの種別によってwhen文を定義することができる
                when(view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }

    }
}