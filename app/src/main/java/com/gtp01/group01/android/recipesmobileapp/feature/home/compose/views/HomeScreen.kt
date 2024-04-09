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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantResponseCode
import com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel.HomeViewModel
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function that constructs the Home screen UI, displaying filtered recipes,
 * search functionality, and handling network errors.
 *
 * @param homeViewModel: Instance of [HomeViewModel] to manage data and state.
 * @param navigateToViewRecipe: Function to navigate to the View Recipe screen for a selected recipe.
 * @param onKeyboardSearch: Function to handle keyboard search events.
 * @param onFilterByCategory: Function to handle category filter selections.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToViewRecipe: (Int) -> Unit,
    onKeyboardSearch: (String) -> Unit,
    onFilterByCategory: (Int) -> Unit
) {
    // Observing network availability state to recomposition whenever the network state changes (available/unavailable)
    val networkAvailable by homeViewModel.networkAvailable.observeAsState(true)

    // Collecting time-based recipe list state
    val timeBasedRecipeListState = homeViewModel.timeBasedRecipeListState.collectAsState()

    // Collecting calorie-based recipe list state
    val calorieBasedRecipeListState = homeViewModel.calorieBasedRecipeListState.collectAsState()

    // Observing user LiveData to get logged-in user information
    val savedUser by homeViewModel.savedUser.collectAsState()

    // Mutable state variables with default values
    var isNetworkAvailable by remember { mutableStateOf(true) }
    isNetworkAvailable = networkAvailable

    // Data fetching logic triggered when the network is available.
    if (isNetworkAvailable) {
        homeViewModel.updateFilters(
            loggedUserId = savedUser.idUser,
            maxCalorie = savedUser.preferCalorie,
            maxDuration = savedUser.preferDuration
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

            // Section to show welcome message to user
            WelcomeMessageSection(savedUser.fullName)
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
            CategorySection(onFilterByCategory = onFilterByCategory)
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Display error message or Home screen content based on network availability.
            if (!isNetworkAvailable) {
                UnavailableNetworkErrorSection(errorCode = ConstantResponseCode.IOEXCEPTION,
                    onRetry = {
                        homeViewModel.updateFilters(
                            loggedUserId = savedUser.idUser,
                            maxCalorie = savedUser.preferCalorie,
                            maxDuration = savedUser.preferDuration
                        )
                    }
                )
            } else {
                // Section for displaying the filtered recipes based on preferred preparation time
                RecipeSuggestionByTimeSection(
                    timeBasedRecipeListState = timeBasedRecipeListState,
                    loggedUserId = savedUser.idUser,
                    timeFilterValue = savedUser.preferDuration,
                    decodeImageToBitmap = { homeViewModel.decodeImageToBitmap(it) },
                    onLikeClicked = homeViewModel::likeRecipe,
                    onRemoveLikeClicked = homeViewModel::removeLikeRecipe,
                    onSuccessfulRecipeUpdate = {
                        homeViewModel.filterRecipesByCalorie(
                            savedUser.idUser,
                            savedUser.preferCalorie
                        )
                    },
                    navigateToViewRecipe = navigateToViewRecipe
                )

                // Section for displaying the filtered recipes based on preferred calorie count
                RecipeSuggestionByCalorieSection(
                    calorieBasedRecipeListState = calorieBasedRecipeListState,
                    loggedUserId = savedUser.idUser,
                    calorieFilterValue = savedUser.preferCalorie,
                    decodeImageToBitmap = { homeViewModel.decodeImageToBitmap(it) },
                    onLikeClicked = homeViewModel::likeRecipe,
                    onRemoveLikeClicked = homeViewModel::removeLikeRecipe,
                    onSuccessfulRecipeUpdate = {
                        homeViewModel.filterRecipesByDuration(
                            savedUser.idUser,
                            savedUser.preferDuration
                        )
                    },
                    navigateToViewRecipe = navigateToViewRecipe
                )
                Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

                // Section for displaying errors when loading recipes
                HandleRecipeResponseErrorSection(
                    timeRecipeListState = timeBasedRecipeListState,
                    calorieRecipeListState = calorieBasedRecipeListState,
                    onRetryFilterRecipesByDuration = {
                        homeViewModel.filterRecipesByDuration(
                            loggedUserId = savedUser.idUser,
                            maxDuration = savedUser.preferDuration
                        )
                    },
                    onRetryFilterRecipesByCalorie = {
                        homeViewModel.filterRecipesByCalorie(
                            loggedUserId = savedUser.idUser,
                            maxCalorie = savedUser.preferCalorie
                        )
                    },
                )

                // Section for displaying loading progress
                HandleRecipeLoadingSection(
                    timeRecipeListState = timeBasedRecipeListState,
                    calorieRecipeListState = calorieBasedRecipeListState
                )
                Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_navigation_height)))
            }
        }
    }
}

/**
 * Composable function to display the welcome message to user.
 *
 * @param userName String: The user's name
 */
@Composable
fun WelcomeMessageSection(userName: String?) {
    Column(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.editText_height))
            .padding(vertical = dimensionResource(id = R.dimen.padding))
    ) {
        Text(
            text = stringResource(R.string.hello, if (userName.isNullOrEmpty()) "" else userName),
            style = MaterialTheme.typography.headlineMedium,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold
        )
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