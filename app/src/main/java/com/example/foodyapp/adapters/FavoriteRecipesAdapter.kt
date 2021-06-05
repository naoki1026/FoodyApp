package com.example.foodyapp.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyapp.R
import com.example.foodyapp.data.database.entities.FavoriteEntity
import com.example.foodyapp.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foodyapp.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.example.foodyapp.util.RecipesDiffUtil
import com.example.foodyapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.favoriteRecipesRowLayout

// Adapterにセルをクリックした際の処理内容を記述していく
//　Adapterはリストとデータのつなぎ、セルとセルに表示するデータのつなぎ
//  RecyclerViewは、大きなデータセットを表示できるコンテナでLitViewをさらに進化させたもの
class FavoriteRecipesAdapter(
    // API Level10以前のActivityにはFragmentを操作するAPIが用意されていないため、
    // その場合はFragmentActivityを使う必要がある
    private val requireActivity : FragmentActivity,
    private val mainViewModel : MainViewModel
) : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    // セルを定義する
    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoritesRecipes = emptyList<FavoriteEntity>()
    private var multiSelection = false
    private lateinit var mActionMode: ActionMode


    // リスト1行分のView
    class MyViewHolder(val binding : FavoriteRecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEntity: FavoriteEntity) {
            binding.favoritesEntity = favoriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from (parent: ViewGroup) : MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)

        val currentRecipe = favoritesRecipes[position]
        holder.bind(currentRecipe)

        // リサイクルビューのアイテムをクリックした際に、DetailsActivityに遷移する
        /** Single Click Listener */
        holder.itemView.favoriteRecipesRowLayout.setOnClickListener {
            val action = FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                currentRecipe.result
            )
            holder.itemView.findNavController().navigate(action)
        }

        /** Long Click Listener */
        holder.itemView.favoriteRecipesRowLayout.setOnLongClickListener {
            println("multiSelection : ${multiSelection}")
            if(!multiSelection) {
                multiSelection = true
                println("multiSelection2 : ${multiSelection}")
                // onCreateActionModeを起動させる
                requireActivity.startActionMode(this)
                applySelection( holder, currentRecipe)
                true
            } else {

                // 以下2つのコードがおかしかった
                applySelection( holder, currentRecipe)
                true
            }
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoriteEntity) {
        println("selectedRecipes : ${selectedRecipes.contains(currentRecipe)}" )
        println("currentRecipe : ${currentRecipe}")
        println("selectedRecipes : ${selectedRecipes}")
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            println("addCurrentRecipe : ${currentRecipe}")
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor : Int, strokeColor: Int) {
        holder.itemView.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.itemView.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle(){
        when(selectedRecipes.size){
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoritesRecipes.size
    }

    private fun applyStatusBarColor(color : Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    // Long Click Listenerによって実行される
    // 黒い背景はデフォルトの状態となっている
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = mode!!

        // ステータスバーは一番上のバーのこと
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach{ holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor )
        }
        multiSelection = false
        selectedRecipes.clear()
        // ステータスバーは一番上のバーのこと
        applyStatusBarColor(R.color.statusBarColor)
    }

    // 2つのリストを比較して、その差分を計算する
    fun setData(newFavoriteRecipes: List<FavoriteEntity> ){
        val recipesDiffUtil = RecipesDiffUtil(favoritesRecipes ,newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        favoritesRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}