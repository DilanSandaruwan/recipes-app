package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel.HomeViewModel
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result

/**
 * Composable function for displaying the Home screen UI.
 *
 * @param homeViewModel The [HomeViewModel] instance to retrieve data and manage UI state.
 * @param navigateToViewRecipe Function to navigate to the View Recipe screen.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToViewRecipe: (Int) -> Unit
) {
    // Collecting time-based recipe list state
    val timeRecipeListState = homeViewModel.timeBasedRecipeListState.collectAsState()

    // Observing user LiveData to get logged-in user information
    val user by homeViewModel.user.observeAsState(null)

    // Mutable state variables for filtering preferences
    var timeFilterValue by remember { mutableStateOf(30) }
    var calorieFilterValue by remember { mutableStateOf(300) }

    // Mutable state variables for user information
    var userId by remember { mutableIntStateOf(0) }
    var preferDuration by remember { mutableIntStateOf(30) }
    var preferCalorie by remember { mutableIntStateOf(300) }

    // Fetch recipes based on user's preferences when user data changes
    LaunchedEffect(user) {
        userId = user?.idUser ?: 0
        preferDuration = user?.preferDuration ?: timeFilterValue
        preferCalorie = user?.preferCalorie ?: calorieFilterValue

        homeViewModel.filterRecipesByDuration(userId, preferDuration)
    }

    // Monitor network availability
    checkNetworkConnectivity(onRetry = {
        homeViewModel.filterRecipesByDuration(
            userId,
            preferDuration
        )
    })

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
            when (val timeRecipeState = timeRecipeListState.value) {
                is Result.Success -> {
                    RecipeSuggestionByTimeSection(
                        timeBasedRecipeList = timeRecipeState.result,
                        timeFilterValue = timeFilterValue,
                        decodeImageToBitmap = { homeViewModel.decodeImageToBitmap(it) },
                        navigateToViewRecipe = navigateToViewRecipe
                    )
                }

                is Result.Failure -> {
                    ShowError(
                        errorCode = timeRecipeState.error,
                        onRetry = { homeViewModel.filterRecipesByDuration(userId, preferDuration) }
                    )
                }

                is Result.Loading -> {
                    ShowLoading()
                }
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_navigation_height)))
        }
    }
}

/**
 * Composable function to monitor network connectivity changes and trigger actions accordingly.
 *
 * @param onRetry Callback function to be invoked when network connectivity is restored.
 */
@Composable
fun checkNetworkConnectivity(
    onRetry: () -> Unit
) {
    // State to track network availability
    val networkAvailable = remember { mutableStateOf(true) }

    // Monitor network connectivity changes
    val localContext = LocalContext.current
    DisposableEffect(Unit) {
        val connectivityManager =
            localContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                networkAvailable.value = true
                // Trigger auto-refresh
                onRetry()
            }
            override fun onLost(network: Network) {
                networkAvailable.value = false
            }
        }
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // Unregister callback when the composable is disposed
        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}

/**
 * Composable function for displaying an error message.
 *
 * @param errorCode The error code to determine the error message.
 * @param modifier Optional modifier for styling.
 * @param onRetry Callback function to retry the operation.
 */
@Composable
fun ShowError(
    errorCode: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            Text(text = stringResource(id = getErrorMessageForCode(errorCode)))
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            Button(onClick = onRetry) {
                Text(stringResource(id = R.string.retry))
            }
        }
    }
}

/**
 * Function to retrieve localized error message for the provided error code.
 *
 * @param errorCode The error code to determine the error message.
 * @return The resource ID of the corresponding error message.
 */
@Composable
fun getErrorMessageForCode(errorCode: String): Int {
    return when (errorCode) {
        "404" -> R.string.home_error_404
        "500" -> R.string.home_error_404
        "IOException" -> R.string.home_error_ioexception
        else -> R.string.home_error_exception
    }
}

/**
 * Composable function for displaying a loading indicator.
 *
 * @param modifier Optional modifier for styling.
 */
@Composable
fun ShowLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.home_circular_progress_indicator_size))
                .align(Alignment.Center)
        )
    }
}