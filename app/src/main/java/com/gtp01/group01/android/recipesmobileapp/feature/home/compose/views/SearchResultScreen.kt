package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel.SearchResultViewModel
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function to display search results based on the entered recipe name.
 *
 * @param recipeName The name of the recipe entered by the user for searching.
 * @param searchResultViewModel The ViewModel responsible for managing search results.
 * @param navigateToViewRecipe Lambda function to navigate to the details of a selected recipe.
 * @param modifier The modifier for customizing the layout of the SearchResultScreen.
 */
@Composable
fun SearchResultScreen(
    recipeName: String,
    searchResultViewModel: SearchResultViewModel = hiltViewModel(),
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Observing user LiveData to get logged-in user information
    val user by searchResultViewModel.user.observeAsState(null)

    // Mutable state variables for user's unique id
    var userId by remember { mutableIntStateOf(0) }

    // Collecting searched recipe list state
    val searchResultRecipeListState =
        searchResultViewModel.searchResultRecipeListState.collectAsState()

    // Fetch recipes based on search criteria
    LaunchedEffect(user) {
        userId = user?.idUser ?: 0
        searchResultViewModel.filterRecipesByName(loggedUserId = userId, recipeName = recipeName)
    }

    // Monitor network availability
    CheckNetworkConnectivity(onRetry = {
        searchResultViewModel.filterRecipesByName(loggedUserId = userId, recipeName = recipeName)
    })

    Surface(
        color = colorResource(id = R.color.md_theme_surfaceContainer),
        shape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.search_screen_corner_radius),
            topEnd = dimensionResource(id = R.dimen.search_screen_corner_radius),
        ),
        modifier = modifier
            .padding(
                top = dimensionResource(id = R.dimen.search_screen_top_padding),
                bottom = dimensionResource(id = R.dimen.search_screen_bottom_padding)
            )
            .fillMaxSize()
    ) {
        // Section for displaying the searched recipes based on recipe name
        if (searchResultRecipeListState.value is Result.Success) {
            val recipeResult = searchResultRecipeListState.value as Result.Success<List<Recipe>>
            SearchRecipeGrid(
                searchResultList = recipeResult.result,
                decodeImageToBitmap = { searchResultViewModel.decodeImageToBitmap(it) },
                navigateToViewRecipe = navigateToViewRecipe
            )
        }

        // Section for displaying errors when loading recipes
        HandleRecipeResponseErrorSection(
            searchResultRecipeListState = searchResultRecipeListState,
            onRetrySearchRecipes = {
                searchResultViewModel.filterRecipesByName(
                    loggedUserId = userId,
                    recipeName = recipeName
                )
            }
        )

        // Section for displaying loading progress
        HandleRecipeLoadingSection(searchResultRecipeListState = searchResultRecipeListState)
    }
}

/**
 * Composable function to display the grid of recipe suggestions based on calorie.
 *
 * @param searchResultList List<Recipe>: The list of recipes to display in this section.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun SearchRecipeGrid(
    searchResultList: List<Recipe>,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.content_horizontal_margin)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.activity_vertical_margin))
            .fillMaxHeight()
    ) {
        items(searchResultList) {
            SearchRecipeCard(
                recipe = it,
                decodeImageToBitmap = decodeImageToBitmap,
                navigateToViewRecipe = navigateToViewRecipe
            )
        }
    }
}

/**
 * Composable function to display a single recipe card based on calorie.
 *
 * @param recipe Recipe: The recipe to display.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun SearchRecipeCard(
    recipe: Recipe,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { navigateToViewRecipe(recipe.idRecipe) }
            .padding(top = dimensionResource(id = R.dimen.search_grid_top_padding))
    ) {
        // section for displaying recipe image
        Column(
            modifier = modifier
                .width(dimensionResource(id = R.dimen.search_recipe_card_width))
                .zIndex(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            recipe.photo?.let {
                val bitmap: Bitmap? = decodeImageToBitmap(recipe.photo.toString())
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = modifier
                            .size(
                                width = dimensionResource(id = R.dimen.search_recipe_card_img_width),
                                height = dimensionResource(id = R.dimen.search_recipe_card_img_height)
                            )
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))
                    )
                } ?: run {
                    Image(
                        painter = painterResource(R.drawable.error_image),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = modifier
                            .size(
                                width = dimensionResource(id = R.dimen.search_recipe_card_img_width),
                                height = dimensionResource(id = R.dimen.search_recipe_card_img_height)
                            )
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))
                    )
                }
            } ?: run {
                Image(
                    painter = painterResource(R.drawable.error_image),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = modifier
                        .size(
                            width = dimensionResource(id = R.dimen.search_recipe_card_img_width),
                            height = dimensionResource(id = R.dimen.search_recipe_card_img_height)
                        )
                        .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))
                )
            }
        }

        // favorite icon placed on top of the image
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(dimensionResource(id = R.dimen.search_favorite_icon_box_size))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_favorite_icon_box_corner_radius)))
                .background(colorResource(id = R.color.md_theme_outlineVariant))
                .zIndex(2f),
        ) {
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = colorResource(id = R.color.md_theme_outline),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.search_favorite_icon_size))
                    .align(Alignment.Center)
            )
        }

        //section for displaying recipe details
        Column(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.search_recipe_card_width))
                .padding(top = dimensionResource(id = R.dimen.search_recipe_card_text_top_padding))
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(colorResource(id = R.color.md_theme_onPrimary))
                    .padding(
                        top = dimensionResource(id = R.dimen.search_recipe_card_text_top_padding),
                        bottom = dimensionResource(id = R.dimen.search_recipe_card_text_bottom_padding)
                    )
                    .padding(horizontal = dimensionResource(id = R.dimen.search_recipe_card_text_bottom_padding))
                    .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))
            ) {
                //section for displaying recipe name
                Text(
                    text = recipe.recipeName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.md_theme_onSurface),
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.height(dimensionResource(id = R.dimen.search_recipe_card_name_label_height))

                )

                //section for displaying recipe calorie
                Row(
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.calorie_kcal, recipe.calorie),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(id = R.color.md_theme_outline_mediumContrast),
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier.height(dimensionResource(id = R.dimen.search_recipe_card_name_label_height))
                    )
                }

                //section for displaying recipe likes
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = null,
                        tint = colorResource(id = R.color.md_theme_outline_mediumContrast),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.search_like_icon_size))
                    )
                    Text(
                        text = stringResource(R.string.like_count, recipe.likeCount),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(id = R.color.md_theme_onSurface),
                    )
                }
            }
        }
    }
}

/**
 * Handles displaying error messages related to recipe searching.
 *
 * @param searchResultRecipeListState The state representing the searched recipe list.
 * @param onRetrySearchRecipes Callback function to retry searching recipes.
 */
@Composable
private fun HandleRecipeResponseErrorSection(
    searchResultRecipeListState: State<Result<List<Recipe>>>,
    onRetrySearchRecipes: () -> Unit,
) {
    val searchResultError = searchResultRecipeListState.value is Result.Failure
    if (searchResultError) {
        val searchResultRecipeState = searchResultRecipeListState.value as Result.Failure
        Column(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.activity_vertical_margin))
        ) {
            ShowError(
                errorCode = searchResultRecipeState.error,
                onRetry = { onRetrySearchRecipes }
            )
        }
    }
}

/**
 * Handles displaying loading indicator while searching recipes.
 *
 * @param timeRecipeListState The state representing the time-based recipe list.
 * @param calorieRecipeListState The state representing the calorie-based recipe list.
 */
@Composable
private fun HandleRecipeLoadingSection(
    searchResultRecipeListState: State<Result<List<Recipe>>>,
) {
    val searchResultLoading = searchResultRecipeListState.value is Result.Loading
    if (searchResultLoading) {
        ShowLoading()
    }
}

/**
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewSearchResultScreen() {
    MaterialTheme {
        SearchResultScreen(
            recipeName = "berry",
            navigateToViewRecipe = { null }
        )
    }
}