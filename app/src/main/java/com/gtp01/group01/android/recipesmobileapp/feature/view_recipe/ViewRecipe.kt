package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentViewRecipeBinding
import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewRecipe : Fragment() {

    private var binding: FragmentViewRecipeBinding? = null
    private lateinit var viewModel: ViewRecipeViewModel
    private val tabTitles = arrayListOf("Ingredients","Instructions")
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


        setUpTabLayoutWithViewPager()

    }

    private fun setUpTabLayoutWithViewPager() {
        binding?.viewpager?.adapter = ViewPagerAdapter(this)
        binding?.let {
            TabLayoutMediator(it.tabLayout, binding!!.viewpager){
                tab,position->
                tab.text= tabTitles[position]

            }.attach()
        }
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