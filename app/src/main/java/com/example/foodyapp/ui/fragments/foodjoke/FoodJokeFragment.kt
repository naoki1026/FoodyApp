package com.example.foodyapp.ui.fragments.foodjoke

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyapp.databinding.FragmentFoodJokeBinding

class FoodJokeFragment : Fragment() {

    private var _binding : FragmentFoodJokeBinding? = null
    private val  binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodJokeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}