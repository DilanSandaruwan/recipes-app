package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentViewRecipeBinding
import com.gtp01.group01.android.recipesmobileapp.shared.common.ResultState
import com.gtp01.group01.android.recipesmobileapp.shared.common.gone
import com.gtp01.group01.android.recipesmobileapp.shared.common.show
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment to display recipe details including instructions, ingredients, and other information.
 */
@AndroidEntryPoint
class ViewRecipe : Fragment() {

    private var binding: FragmentViewRecipeBinding? = null
    private lateinit var viewModel: ViewRecipeViewModel
    private val tabTitles = arrayListOf("Instructions", "Ingredients")
    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewRecipeBinding.inflate(layoutInflater)
        return binding?.root


    }

    /**
     * Initializes the view components and sets up necessary observers.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewRecipeViewModel::class.java]
        setUpTabLayoutWithViewPager()

        val idLoggedUser = 10
        val idrecipe = 1


        // Make the network request to fetch recipe details
        viewModel.fetchRecipeDetail(idLoggedUser, idrecipe)

        initObservers()

        binding?.appBar?.setExpanded(true, true)
        binding?.collapsingToolbarLayout?.apply {

            setCollapsedTitleTextColor(Color.WHITE)
            setExpandedTitleColor(Color.WHITE)
        }
        binding?.imgToolbarBtnBack?.setOnClickListener{
            requireActivity().onBackPressed()
        }

    }
    /**
     * Sets up the tab layout with the ViewPager for switching between instructions and ingredients.
     */
    private fun setUpTabLayoutWithViewPager() {
        this.binding?.viewpager?.adapter = ViewPagerAdapter(this)

        binding?.let {
            TabLayoutMediator(it.tabLayout, binding!!.viewpager) { tab, position ->

                tab.text = tabTitles[position]

            }.attach()
        }
        for (i in 0..2) {

            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null)
                    as TextView
            binding?.tabLayout?.getTabAt(i)?.customView = textView
        }
    }
    /**
     * Initializes observers to observe changes in recipe details.
     */
    private fun initObservers() {


        viewModel.recipeDetails.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding?.progressBar?.show()
                }

                is ResultState.Success<*> -> {
                    binding?.progressBar?.gone()
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
                        val formattedTime = "Time: $time min"
                        binding?.tvLikeCount?.text = formattedLikes
                        binding?.tvRecipeName?.text = recipe_name
                        binding?.carbsTextView?.text = formattedCarbs
                        binding?.calorieTextView?.text = formattedCalorie
                        binding?.proteinTextView?.text = formattedProtein
                        val recipeImageBitmap = recipe.bitmap
                        if (recipeImageBitmap != null) {
                            binding?.let {binding ->
                                val imageView = binding.ivRecipeImage
                                val imageLoader = ImageLoader.Builder(requireContext()).build()
                                val request = ImageRequest.Builder(requireContext())
                                    .data(recipeImageBitmap)
                                    .target(imageView)
                                    .error(R.drawable.error_image) // Error image if loading fails
                                    .build()

                                imageLoader.enqueue(request)
                            }
                        }
                        // Set user's full name to appropriate TextView
                        binding?.tvUserName?.text = userName
                        binding?.tvTime?.text = formattedTime


                    }
                }

                is ResultState.Failure -> {
                    binding?.progressBar?.gone()
                    binding?.let {
                        Snackbar.make(it.root, result.error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
    /**
     * Clears the binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Release resources or references to avoid memory leaks
        // For example, set bindings to null
        binding = null

    }


}