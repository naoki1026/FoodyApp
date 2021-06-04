package com.example.foodyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foodyapp.R
import com.example.foodyapp.adapters.PagerAdapter
import com.example.foodyapp.data.database.entities.FavoriteEntity
import com.example.foodyapp.databinding.ActivityDetailsBinding
import com.example.foodyapp.ui.fragments.ingredients.IngredientsFragment
import com.example.foodyapp.ui.fragments.instructions.InstructionsFragment
import com.example.foodyapp.ui.fragments.overview.OverviewFragment
import com.example.foodyapp.util.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foodyapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var _binding : ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    // argsはargumentsの略で、引数を意味する
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel : MainViewModel by viewModels()
    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 戻るボタンを表示させる
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        // ここでタブにタイトルを定義している
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    // お気に入りとして保存されている場合は黄色くして、保存されていない場合は黄色くしない
    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteEntity.observe( this, { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    // args.result.recipeIdは、navigationが正しく設定されていなかった
                        Log.d("DetailACtivity", "savedRecipe : ${savedRecipe.result.recipeId}")
                        Log.d("DetailACtivity", "args : ${args.result.recipeId}")
                    if (savedRecipe.result.recipeId == args.result.recipeId ){
                        changeMenuItemColor(menuItem, R.color.yellow)
                        recipeSaved = true
                    } else {
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }

        })
    }


    // 戻るボタンを押下した際の処理内容を記述
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoriteEntity (
            0,
            args.result
                )
        mainViewModel.insertFavorites(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipes saved")
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem){
        val favoritesEntity = FavoriteEntity(
            savedRecipeId,
            args.result
        )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from favorites.")
        recipeSaved = false
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make (
            detailsLayout, message, Snackbar.LENGTH_SHORT
        ).setAction("OK"){}.show()
    }
}