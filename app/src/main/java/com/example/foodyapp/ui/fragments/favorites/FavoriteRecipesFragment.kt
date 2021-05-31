package com.example.foodyapp.ui.fragments.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyapp.databinding.FragmentFavoriteRecipesBinding

class FavoriteRecipesFragment : Fragment() {

    private var _binding : FragmentFavoriteRecipesBinding? = null
    private val  binding  get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteRecipesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}