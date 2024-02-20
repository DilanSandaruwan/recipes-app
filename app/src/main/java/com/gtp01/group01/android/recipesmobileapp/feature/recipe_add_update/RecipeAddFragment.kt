package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentRecipeAddBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for adding a new recipe.
 */
@AndroidEntryPoint
class RecipeAddFragment : Fragment() {
    private var _binding: FragmentRecipeAddBinding? = null
    private val viewModel: RecipeAddUpdateViewModel by viewModels()

    // Lazily initialize binding using the _binding property
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_add, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            addRecipeVM = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            // Observe nutrition data changes
            nutrition.observe(viewLifecycleOwner) {
                // TODO: Attach calory constraints with saving recipe item
                Log.e("CHECK", "onViewCreated: ${it[0].name}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Nullify the binding to avoid memory leaks
        _binding = null
    }
}