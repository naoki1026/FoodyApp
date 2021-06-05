package com.example.foodyapp.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyapp.R
import com.example.foodyapp.adapters.FavoriteRecipesAdapter
import com.example.foodyapp.databinding.FragmentFavoriteRecipesBinding
import com.example.foodyapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

// Hiltがつけられたクラスがセットされて、@AndroidEntryPointアノテーションがつけられた他のAndroidクラスに依存関係を提供できる

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    // by viewModels()を記述することでviewModelを初期化している
    private val mainViewModel : MainViewModel by viewModels()

    // mainViewModelをmAdapter内で使用するため
    // FavoriteRecipesAdapterにも、private val mainViewModel : MainViewModel by viewModels()を定義する
    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }


    private var _binding : FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setHasOptionsMenu(true)
        setUpRecyclerView(binding.favoriteRecipesRecyclerView)
        mainViewModel.readFavoriteEntity.observe(viewLifecycleOwner, { favoriteEntity ->
            mAdapter.setData(favoriteEntity)
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.deleteAllFavoriteRecipesMenu){
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar("All recipes removed.")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showSnackBar(message : String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("OK"){}.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}