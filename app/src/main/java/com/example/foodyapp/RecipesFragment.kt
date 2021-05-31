package com.example.foodyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyapp.databinding.FragmentFoodJokeBinding
import com.example.foodyapp.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    private var _binding : FragmentRecipesBinding? = null
    private val  binding  get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(layoutInflater)
        binding.shimmerRecyclerView.showShimmer()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}