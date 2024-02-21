package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.common.gone
import com.gtp01.group01.android.recipesmobileapp.shared.common.show
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentIngredientsBinding
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result.Success
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientsFragment : Fragment() {
    private lateinit var binding: FragmentIngredientsBinding
    private lateinit var viewModel: ViewRecipeViewModel
    private lateinit var adapter: IngredientsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentIngredientsBinding.inflate(layoutInflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(ViewRecipeViewModel::class.java)

        binding.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }

        // Initialize the adapter with an empty list
        adapter = IngredientsAdapter(requireContext(), emptyArray())
        binding.ingredientsRecyclerView.adapter = adapter

        initObservers()

        viewModel.fetchRecipeDetail(idLoggedUser = 1, recipeName = "Spaghetti Bolognese")
    }

    private fun initObservers() {
        viewModel.recipeDetails.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Success<*> -> {
                    binding.progressBar.gone()
                    val data = result.result

                    if (data is List<*>) {
                        val recipes = data.filterIsInstance<Recipe>()
                        if (recipes.isNotEmpty()) {
                            // Assuming you want to display the ingredients from the first recipe in the list
                            val ingredients = recipes[0].ingredients
                            val formattedIngredients = ingredients.split("\\n").toTypedArray()
                            Log.d(TAG, "Recipe ingredients fetched: $ingredients")
                            adapter.updateIngredients(formattedIngredients )
                        } else {
                            // Handle case where no recipes are returned
                            Log.d(TAG, "No recipes found")
                            // You can show a message indicating no recipes found here
                            // binding.textView3.text = "No recipes found"
                        }
                    }
                }
                is Result.Failure -> {
                    binding.progressBar.gone()
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        private val TAG = "Ingradientsfragment"
    }}