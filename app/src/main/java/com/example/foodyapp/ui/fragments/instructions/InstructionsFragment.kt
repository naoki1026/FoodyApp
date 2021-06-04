package com.example.foodyapp.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foodyapp.R
import com.example.foodyapp.databinding.FragmentInstructionsBinding
import com.example.foodyapp.models.Result
import com.example.foodyapp.util.Constants

class InstructionsFragment : Fragment() {

    private var _binding : FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val view = inflater.inflate(R.layout.fragment_instructions, container, false)
        val args = arguments
        val myBundle : Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)
        _binding = FragmentInstructionsBinding.inflate(layoutInflater, container, false)

        binding.instructionsWebView.webViewClient = object : WebViewClient() {}
        val webSiteUrl : String = myBundle!!.sourceUrl
        println(webSiteUrl)
        binding.instructionsWebView.loadUrl(webSiteUrl)

        return binding.root

    }

}