package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentIngredientsBinding
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentProfileBinding
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientsFragment : Fragment() {
    private lateinit var binding: FragmentIngredientsBinding
    private lateinit var viewModel: IngredientsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(IngredientsViewModel::class.java)
        binding = FragmentIngredientsBinding.inflate(layoutInflater)

        return binding.root

    }





}