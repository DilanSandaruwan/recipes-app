package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentIngredientsBinding
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentInstructionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionFragment : Fragment() {

private lateinit var binding : FragmentInstructionBinding
    private lateinit var viewModel: InstructionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(InstructionViewModel::class.java)
        return binding.root

    }



}