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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel.HomeViewModel

/**
 * Composable function for displaying a Home screen layout.
 *
 * @param homeViewModel The view model for managing home screen data.
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
    var filterByTime by remember { mutableIntStateOf(30) }

    // User preferred maximum calorie to filter recipes
    var filterByCalorie by remember { mutableIntStateOf(300) }

    // Trigger recipe filtering and update filterByTime when user changes
    LaunchedEffect(user) {
        user?.let {
            filterByTime = it.preferDuration
            filterByCalorie = it.preferCalorie
            homeViewModel.filterRecipesByDuration(it.idUser, filterByTime)
            homeViewModel.filterRecipesByCalorie(it.idUser, filterByCalorie)
        } ?: run {
            homeViewModel.filterRecipesByDuration(0, filterByTime)
            homeViewModel.filterRecipesByCalorie(0, filterByCalorie)
        }
    }
    MaterialTheme {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .background(color = colorResource(id = R.color.md_theme_inverseOnSurface))
                .padding(horizontal = dimensionResource(id = R.dimen.activity_vertical_margin))
                .padding(top = dimensionResource(id = R.dimen.margin_top), bottom = 60.dp)
        ) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            SearchBarSection(
                searchKeyword = homeViewModel.searchKeyword,
                onSearchKeywordChange = { homeViewModel.updateSearchKeyword(it) },
                onKeyboardDone = {}
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            CategorySection()
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            RecipeSuggestionByTimeSection(
                timeBasedRecipeList = timeBasedRecipeList,
                filterByTime = filterByTime,
                navigateToViewRecipe = navigateToViewRecipe
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            RecipeSuggestionByCalorieSection(
                calorieBasedRecipeList = timeBasedRecipeList,
                filterByCalorie = filterByCalorie
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_navigation_height)))
        }
    }
}