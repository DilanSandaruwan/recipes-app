package com.gtp01.group01.android.recipesmobileapp.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views.SearchResultScreen

/**
 * A Fragment representing the search results screen of the Recipes Mobile App.
 * This Fragment displays a list of recipe previews using Jetpack Compose.
 */
class SearchResultFragment : Fragment() {

    private val navController by lazy { findNavController() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Extracts recipe name and category ID from navigation arguments.
            val args: SearchResultFragmentArgs by navArgs()
            val recipeName = args.recipeName
            val categoryId = args.categoryId

            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            /**
             * Sets the content of the ComposeView with the SearchResultScreen composable.
             * Passes the extracted search criteria and navigation callback.
             */
            setContent {
                SearchResultScreen(
                    recipeName = recipeName,
                    categoryId = categoryId,
                    navigateToViewRecipe = { recipeId ->
                        navController.navigate("com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details.ViewRecipe/$recipeId")
                    }
                )
            }
        }
    }
}