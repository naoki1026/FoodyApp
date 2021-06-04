package com.example.foodyapp.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyapp.R
import com.example.foodyapp.databinding.FragmentIngredientsBinding
import com.example.foodyapp.databinding.FragmentOverviewBinding

class IngredientsFragment : Fragment() {

    private var _binding : FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }
}