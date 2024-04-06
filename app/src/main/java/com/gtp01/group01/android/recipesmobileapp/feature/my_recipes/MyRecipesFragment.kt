package com.gtp01.group01.android.recipesmobileapp.feature.my_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gtp01.group01.android.recipesmobileapp.databinding.FragmentMyRecipesBinding
import com.gtp01.group01.android.recipesmobileapp.feature.main.MainActivity
import com.gtp01.group01.android.recipesmobileapp.feature.my_recipes.compose.MyRecipesScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment responsible for displaying a list of recipes created by the current user.
 * Utilizes Jetpack Compose for UI creation.
 */
@AndroidEntryPoint
class MyRecipesFragment : Fragment() {

    private val navController by lazy { findNavController() }

    // View binding for the fragment
    private var _binding: FragmentMyRecipesBinding? = null

    // Lazily initialize binding using the _binding property
    private val binding get() = _binding!!

    // Reference to the MainActivity
    private lateinit var activity: MainActivity

    // ViewModel for handling recipe addition/update logic
    private val viewModel: MyRecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyRecipesScreen(
                    navigateToViewRecipe = { recipeId ->
                        navController.navigate("com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details.ViewRecipe/$recipeId")
                    }, navigateToUpdateRecipe = { recipeId ->
                        navController.navigate("com.gtp01.group01.android.recipesmobileapp.feature.recipe_delete_update.RecipeUpdateFragment/$recipeId")
                    })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as MainActivity
        activity.setBottomNavVisibility(true) // Make bottom navigation bar visible
    }
}