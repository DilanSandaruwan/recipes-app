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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientsFragment : Fragment() {
    private lateinit var binding: FragmentIngredientsBinding
    private lateinit var viewModel: ViewRecipeViewModel
    private lateinit var ingredientsAdapter: IngredientsAdapter
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

        viewModel.fetchRecipeDetail(idLoggedUser = 1, recipeName = "Spaghetti Bolognese")
        initObservers()
        binding.ingredientsRecyclerView.apply {
            layoutManager= LinearLayoutManager(requireContext())
            ingredientsAdapter = IngredientsAdapter(requireContext(), null)
            binding.ingredientsRecyclerView.adapter = ingredientsAdapter
        }

        // Observe ingredients LiveData

    }






    private fun initObservers(){


        viewModel.recipeDetails.observe(viewLifecycleOwner){result ->
            when (result) {
                is Result.Loading->{
                    binding.progressBar.show()
                }
                is Result.Success<*> ->{
                    binding.progressBar.gone()
                    val ingredients = result.result
                    val myRecycleViewAdapter = IngredientsAdapter(requireContext(), ingredients)
                    binding.ingredientsRecyclerView.adapter = myRecycleViewAdapter
                }

                is Result.Failure->{
                    binding.progressBar.gone()
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                }

            }

        }

    }
    companion object {
        private val TAG = "IngredientsFragment"
    }
}