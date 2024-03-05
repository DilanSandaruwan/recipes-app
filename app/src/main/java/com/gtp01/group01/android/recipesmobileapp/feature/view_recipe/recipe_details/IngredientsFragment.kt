package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentIngredientsBinding
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.common.gone
import com.gtp01.group01.android.recipesmobileapp.shared.common.show
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientsFragment : Fragment() {
    private var binding: FragmentIngredientsBinding? = null
    private lateinit var viewModel: ViewRecipeViewModel
    private lateinit var adapter: IngredientsAdapter
    /**
     * Called to have the fragment instantiate its user interface view.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIngredientsBinding.inflate(layoutInflater)
        return binding!!.root
    }
    /**
     * Called immediately after [onCreateView] has returned, and fragment's view hierarchy has been created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(ViewRecipeViewModel::class.java)

        binding!!.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }

        // Initialize the adapter with an empty list
        adapter = IngredientsAdapter(requireContext(), emptyArray())
        binding!!.ingredientsRecyclerView.adapter = adapter
        // Initialize observers to listen for changes in recipe details
        initObservers()
        // Fetch recipe details
        viewModel.fetchRecipeDetail(idLoggedUser = 10, idrecipe = 1)
    }
    /**
     * Initializes observers to listen for changes in recipe details LiveData.
     */
    private fun initObservers() {
        viewModel.recipeDetails.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show progress bar when loading
                    binding?.progressBar?.show()
                }

                is Result.Success<*> -> {
                    // Hide progress bar when data is loaded successfully
                    binding?.progressBar?.gone()
                    val data = result.result

                    if (data is Recipe) {
                        // Access the recipe directly
                        val recipe = data
                        // Assuming you want to display the ingredients
                        val ingredients = recipe.ingredients
                        // Split ingredients string by newline character and convert it to array
                        val formattedIngredients = ingredients.split("\\n").toTypedArray()

                        // Update UI with ingredients
                        adapter.updateIngredients(formattedIngredients)
                    }
                }

                is Result.Failure -> {
                    // Hide progress bar when there's a failure
                    binding?.progressBar?.gone()
                    // Show error message using Snackbar
                    binding?.root?.let {
                        Snackbar.make(it, result.error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    /**
     * Called when the fragment's view is destroyed.
     * It releases references to the binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Clearing the binding reference to avoid memory leaks
        binding = null
    }

}