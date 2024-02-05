package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentViewRecipeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewRecipe : Fragment() {

    private var binding: FragmentViewRecipeBinding? = null
    private lateinit var viewModel: ViewRecipeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewRecipeViewModel::class.java]
        // TODO: Use the ViewModel



    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Release resources or references to avoid memory leaks
        // For example, set bindings to null
        binding = null
        // Log that the view has been destroyed
        logMessage("View destroyed")
    }
    private fun logMessage(message: String) {
        Log.d("ViewRecipeFragment", message)
    }
}