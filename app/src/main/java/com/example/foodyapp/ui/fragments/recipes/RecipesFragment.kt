package com.example.foodyapp.ui.fragments.recipes

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodyapp.R
import com.example.foodyapp.viewmodels.MainViewModel
import com.example.foodyapp.adapters.RecipesAdapter
import com.example.foodyapp.databinding.FragmentRecipesBinding
import com.example.foodyapp.util.Constants.Companion.API_KEY
import com.example.foodyapp.util.NetworkResult
import com.example.foodyapp.util.observeOnce
import com.example.foodyapp.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding : FragmentRecipesBinding? = null
    private val  binding  get() = _binding!!
    private val  mAdapter  by lazy { RecipesAdapter() }
    private lateinit var mainViewModel : MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModelの取得
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // applicationの代わりに、(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = FragmentRecipesBinding.inflate(layoutInflater)
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)

        // Livedataを使用する際に以下を定義する必要がある
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setUpRecyclerView()
        readDatabase()
        return binding.root
    }

    private fun setUpRecyclerView(){
        binding.shimmerRecyclerView.adapter = mAdapter
        binding.shimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    // データベースにデータが存在する場合は
    private fun readDatabase() {
        // Fragmentではthisを使用するとパフォーマンスが低下してしまう可能性があるため、
        // viewLifecycleOwnerを使用する

        // readRecipesはLiveDataであるため、情報が更新された場合にobserve以降の処理が行われる
        // アプリインストール時に、requestとreadの両方が呼び出されてしまうため、
        // extension functionのonserveOnceを使って、処理が一度しか行われないようにしている
        mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, {database ->
            lifecycleScope.launch {
                if (database.isNotEmpty()) {
                    Log.d("RecipesFragment", "read databse called!")
                    // 2つのリストを比較してその差分を算出する
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        })
    }

    // FoodAPIからデータを取得する
    private fun requestApiData(){
        Log.d("RecipesFragment", "requestAPI data called!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    response.data?.let {
                        Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if(database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun showShimmerEffect(){
        binding.shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.shimmerRecyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}