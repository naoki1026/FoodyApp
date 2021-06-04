package com.example.foodyapp.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodyapp.R
import com.example.foodyapp.adapters.IngredientsAdapter
import com.example.foodyapp.databinding.FragmentIngredientsBinding
import com.example.foodyapp.models.Result
import com.example.foodyapp.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {
     // private var _binding : FragmentIngredientsBinding? = null
     // private val binding get() = _binding!!
    //  RecyclerViewを使うときはbindingではなくて、viewを使うと覚えておけば良いのか？
    private val mAdapter : IngredientsAdapter by lazy {IngredientsAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        // _bindingではなくて、viewにする必要がある
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)
        val args = arguments
        val mBundle : Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        setupRecyclerView(view)
        mBundle?.extendedIngredients?.let {mAdapter.setData(it)}

        // ここをviewにしてあげないと表示されない
        return view
    }

    private fun setupRecyclerView(view: View) {
        view.ingredientsRecyclerview.adapter = mAdapter
        view.ingredientsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

    }
}