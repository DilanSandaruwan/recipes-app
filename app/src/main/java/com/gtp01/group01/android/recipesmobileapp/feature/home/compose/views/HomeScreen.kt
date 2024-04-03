package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantResponseCode
import com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel.HomeViewModel
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function for displaying the Home screen UI.
 *
 * @param homeViewModel The [HomeViewModel] instance to retrieve data and manage UI state.
 * @param navigateToViewRecipe Function to navigate to the View Recipe screen.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToViewRecipe: (Int) -> Unit,
    onKeyboardSearch: (String) -> Unit
) {
    // Observing network availability state to recomposition whenever the network state changes (available/unavailable)
    val networkAvailable by homeViewModel.networkAvailable.observeAsState(true)

    // Collecting time-based recipe list state
    val timeBasedRecipeListState = homeViewModel.timeBasedRecipeListState.collectAsState()

    // Collecting calorie-based recipe list state
    val calorieBasedRecipeListState = homeViewModel.calorieBasedRecipeListState.collectAsState()

    // Observing user LiveData to get logged-in user information
    val user by homeViewModel.user.observeAsState(null)

    // Mutable state variables with default values
    var userId by remember { mutableIntStateOf(0) }
    var preferDuration by remember { mutableIntStateOf(30) }
    var preferCalorie by remember { mutableIntStateOf(300) }
    var isNetworkAvailable by remember { mutableStateOf(true) }

    // Assigning user's preferences when user data changes
    user?.let {
        userId = it.idUser
        preferDuration = it.preferDuration
        preferCalorie = it.preferCalorie
    }

    // Data fetching logic triggered when the network is available.
    isNetworkAvailable = networkAvailable
    if (isNetworkAvailable) {
        homeViewModel.filterRecipesByDuration(
            loggedUserId = userId,
            maxDuration = preferDuration
        )
        homeViewModel.filterRecipesByCalorie(
            loggedUserId = userId,
            maxCalorie = preferCalorie
        )
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
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Section for displaying the search bar
            SearchBarSection(
                searchKeyword = homeViewModel.searchKeyword,
                onSearchKeywordChange = { homeViewModel.updateSearchKeyword(it) },
                isValidKeyword = { homeViewModel.isValidKeyword(it) },
                onKeyboardSearch = onKeyboardSearch,
                onClearButtonClicked = { homeViewModel.clearSearchKeyword() }
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Section for displaying the categories to select
            CategorySection()
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Display error message or Home screen content based on network availability.
            if (!isNetworkAvailable) {
                UnavailableNetworkErrorSection(errorCode = ConstantResponseCode.IOEXCEPTION,
                    onRetry = {
                        homeViewModel.filterRecipesByDuration(
                            loggedUserId = userId,
                            maxDuration = preferDuration
                        )
                        homeViewModel.filterRecipesByCalorie(
                            loggedUserId = userId,
                            maxCalorie = preferCalorie
                        )
                    }
                )
            } else {
                // Section for displaying the filtered recipes based on preferred preparation time
                if (timeBasedRecipeListState.value is Result.Success) {
                    val recipeResult =
                        timeBasedRecipeListState.value as Result.Success<List<Recipe>>
                    RecipeSuggestionByTimeSection(
                        timeBasedRecipeList = recipeResult.result,
                        timeFilterValue = preferDuration,
                        decodeImageToBitmap = { homeViewModel.decodeImageToBitmap(it) },
                        navigateToViewRecipe = navigateToViewRecipe
                    )
                }

                // Section for displaying the filtered recipes based on preferred calorie count
                if (calorieBasedRecipeListState.value is Result.Success) {
                    val recipeResult =
                        calorieBasedRecipeListState.value as Result.Success<List<Recipe>>
                    RecipeSuggestionByCalorieSection(
                        calorieBasedRecipeList = recipeResult.result,
                        calorieFilterValue = preferCalorie,
                        decodeImageToBitmap = { homeViewModel.decodeImageToBitmap(it) },
                        navigateToViewRecipe = navigateToViewRecipe
                    )
                }

                // Section for displaying errors when loading recipes
                HandleRecipeResponseErrorSection(
                    timeRecipeListState = timeBasedRecipeListState,
                    calorieRecipeListState = calorieBasedRecipeListState,
                    onRetryFilterRecipesByDuration = {
                        homeViewModel.filterRecipesByDuration(
                            loggedUserId = userId,
                            maxDuration = preferDuration
                        )
                    },
                    onRetryFilterRecipesByCalorie = {
                        homeViewModel.filterRecipesByCalorie(
                            loggedUserId = userId,
                            maxCalorie = preferCalorie
                        )
                    },
                )
                Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_navigation_height)))

                // Section for displaying loading progress
                HandleRecipeLoadingSection(
                    timeRecipeListState = timeBasedRecipeListState,
                    calorieRecipeListState = calorieBasedRecipeListState
                )
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_navigation_height)))
        }
    }
}

/**
 * Handles displaying error message related to unavailable network connection.
 *
 * @param errorCode The error code to determine the error message.
 * @param onRetry Callback function to retry the operation.
 */
@Composable
private fun UnavailableNetworkErrorSection(
    errorCode: String,
    onRetry: () -> Unit
) {
    ShowError(
        errorCode = errorCode,
        onRetry = onRetry
    )
}

/**
 * Handles displaying error messages related to recipe fetching for both time-based and calorie-based recipes.
 *
 * @param timeRecipeListState The state representing the time-based recipe list.
 * @param calorieRecipeListState The state representing the calorie-based recipe list.
 * @param onRetryFilterRecipesByDuration Callback function to retry fetching recipes based on duration.
 * @param onRetryFilterRecipesByCalorie Callback function to retry fetching recipes based on calorie count.
 */
@Composable
private fun HandleRecipeResponseErrorSection(
    timeRecipeListState: State<Result<List<Recipe>>>,
    calorieRecipeListState: State<Result<List<Recipe>>>,
    onRetryFilterRecipesByDuration: () -> Unit,
    onRetryFilterRecipesByCalorie: () -> Unit
) {
    val timeError = timeRecipeListState.value is Result.Failure
    val calorieError = calorieRecipeListState.value is Result.Failure

    if (timeError && calorieError) {
        val timeRecipeState = timeRecipeListState.value as Result.Failure
        ShowError(
            errorCode = timeRecipeState.error,
            onRetry = onRetryFilterRecipesByDuration
        )
    } else {
        if (timeError) {
            val timeRecipeState = timeRecipeListState.value as Result.Failure
            ShowError(
                errorCode = timeRecipeState.error,
                onRetry = onRetryFilterRecipesByDuration
            )
        }
        if (calorieError) {
            val calorieRecipeState = calorieRecipeListState.value as Result.Failure
            ShowError(
                errorCode = calorieRecipeState.error,
                onRetry = onRetryFilterRecipesByCalorie
            )
        }
    }
}

/**
 * Handles displaying loading indicator while fetching time-based and calorie-based recipes.
 *
 * @param timeRecipeListState The state representing the time-based recipe list.
 * @param calorieRecipeListState The state representing the calorie-based recipe list.
 */
@Composable
private fun HandleRecipeLoadingSection(
    timeRecipeListState: State<Result<List<Recipe>>>,
    calorieRecipeListState: State<Result<List<Recipe>>>,
) {
    val timeLoading = timeRecipeListState.value is Result.Loading
    val calorieLoading = calorieRecipeListState.value is Result.Loading

    if (timeLoading || calorieLoading) {
        ShowLoading()
    }
}

/**
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewUnavailableNetworkErrorSection() {
    MaterialTheme {
        UnavailableNetworkErrorSection(
            errorCode = "400",
            onRetry = { }
        )
    }
}