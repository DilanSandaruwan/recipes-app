package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import android.content.ContentValues
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.tabs.TabLayoutMediator
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentViewRecipeBinding
import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewRecipe : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding
    private lateinit var viewModel: ViewRecipeViewModel
    private val tabTitles = arrayListOf("Instructions","Ingredients")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewRecipeBinding.inflate(layoutInflater)
        return binding.root


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewRecipeViewModel::class.java]
        setUpTabLayoutWithViewPager()
        val idLoggedUser = 10
        val recipeName = "Spaghetti Bolognese"

        // Log the request parameters
        Log.d(ContentValues.TAG, "Fetching recipe details for user id: $idLoggedUser, recipe name: $recipeName")

        // Make the network request to fetch recipe details
        viewModel.fetchRecipeDetail(idLoggedUser, recipeName)

    }

    private fun setUpTabLayoutWithViewPager() {
        this.binding.viewpager.adapter = ViewPagerAdapter(this)

            TabLayoutMediator(binding.tabLayout, binding.viewpager){ tab,position->

                tab.text= tabTitles[position]

            }.attach()
        for (i in 0..2){

            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title,null)
            as TextView
            binding.tabLayout.getTabAt(i)?.customView = textView
        }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        // Release resources or references to avoid memory leaks
        // For example, set bindings to null

        // Log that the view has been destroyed
        logMessage("View destroyed")
    }
    private fun logMessage(message: String) {
        Log.d("ViewRecipeFragment", message)
    }
}