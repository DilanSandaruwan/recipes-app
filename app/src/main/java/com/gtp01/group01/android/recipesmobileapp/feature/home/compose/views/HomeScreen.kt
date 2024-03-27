package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel.HomeViewModel

/**
 * Composable function for displaying the Home screen layout.
 *
 * @param homeViewModel The view model for managing home screen data.
 * @param navigateToViewRecipe Callback to navigate to a recipe details screen.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToViewRecipe: (Int) -> Unit
) {
    val timeBasedRecipeList by homeViewModel.timeBasedRecipeList.observeAsState(emptyList())
    val calorieBasedRecipeList by homeViewModel.calorieBasedRecipeList.observeAsState(emptyList())

    // Observe user changes
    val user by homeViewModel.user.observeAsState(null)

    // User preferred time duration to filter recipes
    var timeFilterValue by remember { mutableIntStateOf(30) }

    // User preferred maximum calorie to filter recipes
    var calorieFilterValue by remember { mutableIntStateOf(300) }

    // If user is null, default values are provided for userId, preferDuration, and preferCalorie.
    LaunchedEffect(user) {
        // If user is not null, update filter values and filter recipes based on user preferences
        val userId = user?.idUser ?: 0
        val preferDuration = user?.preferDuration ?: timeFilterValue
        val preferCalorie = user?.preferCalorie ?: calorieFilterValue

        homeViewModel.filterRecipesByDuration(userId, preferDuration)
        homeViewModel.filterRecipesByCalorie(userId, preferCalorie)
    }

    MaterialTheme {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .background(color = colorResource(id = R.color.md_theme_surfaceContainer))
                .padding(horizontal = dimensionResource(id = R.dimen.activity_vertical_margin))
                .padding(
                    top = dimensionResource(id = R.dimen.margin_top),
                    bottom = dimensionResource(id = R.dimen.bottom_navigation_height)
                )
        ) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Section for displaying the search bar
            SearchBarSection(
                searchKeyword = homeViewModel.searchKeyword,
                onSearchKeywordChange = { homeViewModel.updateSearchKeyword(it) },
                onKeyboardDone = {}
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Section for displaying the categories to select
            CategorySection()
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Section for displaying the filtered recipes based on preferred preparation time
            RecipeSuggestionByTimeSection(
                timeBasedRecipeList = timeBasedRecipeList,
                timeFilterValue = timeFilterValue,
                decodeImageToBitmap = { homeViewModel.decodeImageToBitmap(it) },
                navigateToViewRecipe = navigateToViewRecipe
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Section for displaying the filtered recipes based on preferred calorie
            RecipeSuggestionByCalorieSection(
                calorieBasedRecipeList = calorieBasedRecipeList,
                calorieFilterValue = calorieFilterValue,
                decodeImageToBitmap = { homeViewModel.decodeImageToBitmap(it) },
                navigateToViewRecipe = navigateToViewRecipe
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_navigation_height)))
        }
    }
}