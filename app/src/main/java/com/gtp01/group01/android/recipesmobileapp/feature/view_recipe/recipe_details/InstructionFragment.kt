package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentInstructionBinding
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.common.gone
import com.gtp01.group01.android.recipesmobileapp.shared.common.show
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionFragment : Fragment() {

    private lateinit var binding: FragmentInstructionBinding
    private lateinit var viewModel: ViewRecipeViewModel
    private lateinit var adapter: InstructionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructionBinding.inflate(layoutInflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ViewRecipeViewModel::class.java)

        binding.instructionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }


        // Initialize the adapter with an empty list
        adapter = InstructionAdapter(requireContext(), emptyArray())
        binding.instructionsRecyclerView.adapter = adapter

        initObservers()
        viewModel.fetchRecipeDetail(idLoggedUser = 1, recipeName = "Spaghetti Bolognese")

    }

    private fun initObservers() {


        viewModel.recipeDetails.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }

                is Result.Success<*> -> {
                    binding.progressBar.gone()
                    val data = result.result

                    if (data is List<*>) {
                        val recipes = data.filterIsInstance<Recipe>()
                        if (recipes.isNotEmpty()) {
                            // Assuming you want to display the instruction from the first recipe in the list
                            val instruction = recipes[0].instruction
                            val formattedInstruction = instruction.split("\\n").toTypedArray()
                            Log.d(TAG, "Recipe instruction fetched: $formattedInstruction")
                            adapter.updateInstructions(formattedInstruction)

                        } else {
                            // Handle case where no recipes are returned
                            Log.d(TAG, "No recipes found")

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
        const val TAG = "InstructionFragment"
    }
}