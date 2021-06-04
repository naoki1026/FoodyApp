package com.example.foodyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foodyapp.R
import com.example.foodyapp.adapters.PagerAdapter
import com.example.foodyapp.databinding.ActivityDetailsBinding
import com.example.foodyapp.ui.fragments.ingredients.IngredientsFragment
import com.example.foodyapp.ui.fragments.instructions.InstructionsFragment
import com.example.foodyapp.ui.fragments.overview.OverviewFragment
import com.example.foodyapp.util.Constants.Companion.RECIPE_RESULT_KEY
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var _binding : ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    // argsはargumentsの略で、引数を意味する
    private val args by navArgs<DetailsActivityArgs>()

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

     // 戻るボタンを押下した際の処理内容を記述
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}