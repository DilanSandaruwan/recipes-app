package com.gtp01.group01.android.recipesmobileapp.feature.my_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentMyRecipesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecipesFragment : Fragment() {

    // View binding for the fragment
    private var _binding: FragmentMyRecipesBinding? = null

    // Lazily initialize binding using the _binding property
    private val binding get() = _binding!!

    // ViewModel for handling recipe addition/update logic
    private val viewModel: MyRecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_recipes, container, false)
        return binding.root
    }
}