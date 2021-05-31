package com.example.foodyapp.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodyapp.MainViewModel
import com.example.foodyapp.RecipesAdapter
import com.example.foodyapp.databinding.FragmentRecipesBinding
import com.example.foodyapp.ui.MainActivity
import com.example.foodyapp.util.Constants.Companion.API_KEY
import com.example.foodyapp.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding : FragmentRecipesBinding? = null
    private val  binding  get() = _binding!!
    private val  mAdapter  by lazy {RecipesAdapter()}
    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setUpRecyclerView()
        requiestApiData()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun requiestApiData(){
        mainViewModel.getRecipes(applyQueries())
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

    private fun applyQueries(): HashMap<String, String>{
        val queries : HashMap<String, String> = HashMap()
        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
        println("queries : ${queries}")

        return queries

    }

    private fun setUpRecyclerView(){
        binding.shimmerRecyclerView.adapter = mAdapter
        binding.shimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect(){
        binding.shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.shimmerRecyclerView.hideShimmer()
    }
}