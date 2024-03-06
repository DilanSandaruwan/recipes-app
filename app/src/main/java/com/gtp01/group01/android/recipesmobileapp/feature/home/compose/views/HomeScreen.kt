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
 * Composable function for displaying a Home screen layout.
 *
 * @param homeViewModel The view model for managing home screen data.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val timeBasedRecipeList by homeViewModel.timeBasedRecipeList.observeAsState(emptyList())

    // Observe user changes
    val user by homeViewModel.user.observeAsState(null)

    // User preferred time duration to filter recipes
    var filterByTime by remember { mutableIntStateOf(30) }

    // Trigger recipe filtering and update filterByTime when user changes
    LaunchedEffect(user) {
        user?.let {
            filterByTime = it.preferDuration
            homeViewModel.filterRecipesByDuration(it.idUser, filterByTime)
        } ?: homeViewModel.filterRecipesByDuration(0, filterByTime)
    }
    MaterialTheme {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .background(color = colorResource(id = R.color.md_theme_background))
                .padding(horizontal = dimensionResource(id = R.dimen.activity_vertical_margin))
                .padding(top = dimensionResource(id = R.dimen.margin_top))
        ) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
//            SearchBar()
            RecipeSuggestionByTimeSection(
                timeBasedRecipeList = timeBasedRecipeList,
                filterByTime = filterByTime
            )
        }
    }
}