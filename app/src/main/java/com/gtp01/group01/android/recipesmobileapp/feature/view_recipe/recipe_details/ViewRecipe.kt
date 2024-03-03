package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentViewRecipeBinding
import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details.InstructionFragment.Companion.TAG
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.common.gone
import com.gtp01.group01.android.recipesmobileapp.shared.common.show
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewRecipe : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding
    private lateinit var viewModel: ViewRecipeViewModel
    private val tabTitles = arrayListOf("Instructions", "Ingredients")
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
        val idrecipe = 1

        // Log the request parameters
        Log.d(
            ContentValues.TAG,
            "Fetching recipe details for user id: $idLoggedUser, recipe name: $idrecipe"
        )

        // Make the network request to fetch recipe details
        viewModel.fetchRecipeDetail(idLoggedUser, idrecipe)

        initObservers()

        binding.appBar.setExpanded(true, true)
        binding.collapsingToolbarLayout.apply {
            title = "$idrecipe"



            setCollapsedTitleTextColor(Color.WHITE)
            setExpandedTitleColor(Color.WHITE)
        }


    }

    private fun setUpTabLayoutWithViewPager() {
        this.binding.viewpager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->

            tab.text = tabTitles[position]

        }.attach()
        for (i in 0..2) {

            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null)
                    as TextView
            binding.tabLayout.getTabAt(i)?.customView = textView
        }
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

                    if (data is Recipe) {
                        val recipe = data
                            // Assuming you want to display the instruction from the first recipe in the list

                            val recipe_name = recipe.recipeName
                            val carbs = recipe.carbs
                            val calorie = recipe.calorie
                            val likes = recipe.likeCount
                            val protein = recipe.protein
                            val time = recipe.preparationTime

                            val formattedCarbs = "Carbs: $carbs"
                            val formattedCalorie = "Calorie: $calorie"
                            val formattedProtein = "Protein: $protein"
                            // Access user's full name
                            val userName = recipe.owner.fullName
                            val formattedLikes = "Likes: $likes"
                            val formattedTime = "Time: $time"
                            binding.tvLikeCount.text = formattedLikes
                            binding.tvRecipeName.text = recipe_name
                            binding.carbsTextView.text = formattedCarbs
                            binding.calorieTextView.text = formattedCalorie
                            binding.proteinTextView.text = formattedProtein
                        val recipeImageBitmap = recipe.bitmap
                        if (recipeImageBitmap != null) {
                            Glide.with(requireContext())
                                .load(recipeImageBitmap)
                                .placeholder(R.drawable.img) // Placeholder image while loading
                                .error(R.drawable.error_image) // Error image if loading fails
                                .into(binding.ivRecipeImage)
                        } else {
                            // If the bitmap is null, you may want to set a placeholder image or hide the ImageView
                            // For example:
                            binding.ivRecipeImage.setImageResource(R.drawable.img)
                        }
                            // Set user's full name to appropriate TextView
                            binding.tvUserName.text = userName
                            binding.tvTime.text = formattedTime
                            Log.d(TAG, "Formatted time: ${binding.tvTime.text}")


                }}

                is Result.Failure -> {
                    binding.progressBar.gone()
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                }
            }
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