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
import com.gtp01.group01.android.recipesmobileapp.shared.common.ResultState
import com.gtp01.group01.android.recipesmobileapp.shared.common.gone
import com.gtp01.group01.android.recipesmobileapp.shared.common.show
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionFragment : Fragment() {

    private var binding: FragmentInstructionBinding? = null
    private lateinit var viewModel: ViewRecipeViewModel
    private lateinit var adapter: InstructionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInstructionBinding.inflate(layoutInflater)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ViewRecipeViewModel::class.java)
        // Set up RecyclerView layout manager
        binding!!.instructionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }

        // Initialize the adapter with an empty list
        adapter = InstructionAdapter(requireContext(), emptyArray())
        binding!!.instructionsRecyclerView.adapter = adapter
        // Observe changes in recipe details
        initObservers()
        // Fetch recipe details
        viewModel.fetchRecipeDetail(idLoggedUser = 10, idrecipe = 1)

    }

    private fun initObservers() {
        viewModel.recipeDetails.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Show progress bar when loading
                    binding?.progressBar?.show()
                }

                is ResultState.Success<*> -> {
                    // Hide progress bar when data is loaded successfully
                    binding?.progressBar?.gone()
                    val data = result.result

                    if (data is Recipe) {
                        // Access the recipe directly
                        val recipe = data
                        // Access instruction from the recipe
                        val instruction = recipe.instruction
                        // Split instruction string by newline character and convert it to array
                        val formattedInstruction = instruction.split("\\n").toTypedArray()
                        // Update UI with instructions
                        adapter.updateInstructions(formattedInstruction)
                    } else {
                        // Handle unexpected data type
                        Log.e(TAG, "Unexpected data type: $data")
                    }
                }

                is ResultState.Failure -> {
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


    override fun onDestroyView() {
        super.onDestroyView()
        // Clearing the binding reference
        binding = null

    }
}