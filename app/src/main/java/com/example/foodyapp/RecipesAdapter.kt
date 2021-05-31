package com.example.foodyapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyapp.databinding.RecipesRowLayoutBinding
import com.example.foodyapp.models.FoodRecipe
import com.example.foodyapp.models.Result
import com.example.foodyapp.util.RecipesDiffUtil

class RecipesAdapter() : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()

    class MyViewHolder(val binding : RecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.result = result
            //databindingを使用するときに記述しておかなければいけないらしい・・・
            binding.executePendingBindings()
        }

        companion object {
            fun from (parent: ViewGroup) : MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = recipes[position]

        // 記述し忘れ
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    // 2つのリストを比較して、その差分を計算する
    fun setData(newData: FoodRecipe){
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}