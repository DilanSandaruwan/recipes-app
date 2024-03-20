package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel.HomeViewModel

/**
 * Composable function for displaying a Home screen layout.
 *
 * @param homeViewModel The view model for managing home screen data.
 */
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    val timeBasedRecipeList by homeViewModel.timeBasedRecipeList.observeAsState(emptyList())
    homeViewModel.filterRecipesByDuration(0, 1000)
    MaterialTheme {
        Surface {
            RecipeSuggestionByTimeSection(timeBasedRecipeList)
        }
    }
}