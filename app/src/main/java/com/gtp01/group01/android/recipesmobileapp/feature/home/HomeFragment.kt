package com.gtp01.group01.android.recipesmobileapp.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views.HomeScreen

/**
 * A Fragment representing the home screen of the Recipes Mobile App.
 * This Fragment displays a search bar, filter by category criteria and a list of recipe previews using Jetpack Compose.
 */
class HomeFragment : Fragment() {

    private val navController by lazy { findNavController() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            /**
             * Sets the content of the ComposeView with the HomeScreen composable as the main layout of the home screen.
             * Passes the navigation callbacks.
             */
            setContent {
                HomeScreen(
                    navigateToViewRecipe = { recipeId ->
                        navController.navigate("com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details.ViewRecipe/$recipeId")
                    },
                    onKeyboardSearch = { recipeName ->
                        navController.navigate("com.gtp01.group01.android.recipesmobileapp.feature.home.SearchResultFragment/$recipeName")
                    },
                    onFilterByCategory = { categoryId ->
                        navController.navigate("com.gtp01.group01.android.recipesmobileapp.feature.home.SearchResultFragment/category/$categoryId")
                    }
                )
            }
        }
    }
}