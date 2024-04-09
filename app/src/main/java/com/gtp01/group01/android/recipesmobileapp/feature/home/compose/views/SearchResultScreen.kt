package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantResponseCode
import com.gtp01.group01.android.recipesmobileapp.constant.UserDefaultConstant
import com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel.SearchResultViewModel
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function to display search results based on the entered recipe name.
 *
 * @param recipeName The name of the recipe entered by the user for searching.
 * @param categoryId The category id of the recipe selected by the user for searching.
 * @param searchResultViewModel The ViewModel responsible for managing search results.
 * @param navigateToViewRecipe Lambda function to navigate to the details of a selected recipe.
 * @param modifier The modifier for customizing the layout of the SearchResultScreen.
 */
@Composable
fun SearchResultScreen(
    recipeName: String,
    categoryId: Int,
    searchResultViewModel: SearchResultViewModel = hiltViewModel(),
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Observing network availability state to recomposition whenever the network state changes (available/unavailable)
    val networkAvailable by searchResultViewModel.networkAvailable.observeAsState(true)

    // Observing user LiveData to get logged-in user information
    val savedUser by searchResultViewModel.savedUser.collectAsState()

    // Collecting searched recipe list state
    val searchResultRecipeListState =
        searchResultViewModel.searchResultRecipeListState.collectAsState()

    // Mutable state variables with default values
    var isNetworkAvailable by remember { mutableStateOf(true) }
    isNetworkAvailable = networkAvailable

    // Data fetching logic triggered when the network is available.
    if (isNetworkAvailable) {
        // Filters recipes by category if a valid category ID is provided.
        if (categoryId != 0) {
            searchResultViewModel.filterRecipesByCategory(
                loggedUserId = savedUser.idUser,
                categoryId = categoryId
            )
        } else {
            // Filters recipes by name if no category is selected and a recipe name is provided.
            searchResultViewModel.filterRecipesByName(
                loggedUserId = savedUser.idUser,
                recipeName = recipeName
            )
        }
    }

    // Content for search result screen
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
        // Display error message or Home screen content based on network availability.
        if (!isNetworkAvailable) {
            UnavailableNetworkErrorSection(errorCode = ConstantResponseCode.IOEXCEPTION,
                onRetry = {
                    // Filters recipes by category if a valid category ID is provided.
                    if (categoryId != 0) {
                        searchResultViewModel.filterRecipesByCategory(
                            loggedUserId = savedUser.idUser,
                            categoryId = categoryId
                        )
                    } else {
                        // Filters recipes by name if no category is selected and a recipe name is provided.
                        searchResultViewModel.filterRecipesByName(
                            loggedUserId = savedUser.idUser,
                            recipeName = recipeName
                        )
                    }
                }
            )
        } else {
            // Section for displaying the searched recipes based on recipe name
            if (searchResultRecipeListState.value is Result.Success) {
                val recipeResult = searchResultRecipeListState.value as Result.Success<List<Recipe>>
                SearchRecipeGrid(
                    loggedUserId = savedUser.idUser,
                    searchResultList = recipeResult.result,
                    decodeImageToBitmap = { searchResultViewModel.decodeImageToBitmap(it) },
                    onLikeClicked = searchResultViewModel::likeRecipe,
                    onRemoveLikeClicked = searchResultViewModel::removeLikeRecipe,
                    onFavoriteClicked = searchResultViewModel::addFavoriteRecipe,
                    onRemoveFavoriteClicked = searchResultViewModel::removeFavoriteRecipe,
                    navigateToViewRecipe = navigateToViewRecipe
                )
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))

            // Section for displaying errors when loading recipes
            HandleRecipeResponseErrorSection(
                searchResultRecipeListState = searchResultRecipeListState,
                onRetrySearchRecipes = {
                    // Filters recipes by category if a valid category ID is provided.
                    if (categoryId != 0) {
                        searchResultViewModel.filterRecipesByCategory(
                            loggedUserId = savedUser.idUser,
                            categoryId = categoryId
                        )
                    } else {
                        // Filters recipes by name if no category is selected and a recipe name is provided.
                        searchResultViewModel.filterRecipesByName(
                            loggedUserId = savedUser.idUser,
                            recipeName = recipeName
                        )
                    }
                }
            )

            // Section for displaying loading progress
            HandleRecipeLoadingSection(searchResultRecipeListState = searchResultRecipeListState)
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
 * Composable function to display the grid of recipe suggestions based on calorie.
 *
 * @param loggedUserId [Int]: The ID of the currently logged-in user (used for like/unlike functionality).
 * @param searchResultList List<Recipe>: The list of recipes to display in this section.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param onLikeClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for liking a recipe.
 *     - Takes:
 *        - loggedUserId: The ID of the logged-in user.
 *        - recipeId: The ID of the recipe to like.
 *        - onSuccess: Function to execute upon successful like operation.
 *        - onFailure: Function to execute upon like operation failure.
 *        - onLoading: Function to execute while the like operation is ongoing.
 * @param onRemoveLikeClicked [Function() -> Unit)]: Callback function for removing a like from a recipe.
 *      - Takes the same arguments as `onLikeClicked`.
 * @param onFavoriteClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for adding a recipe to favorites.
 *     - Takes:
 *        - loggedUserId: The ID of the logged-in user.
 *        - recipeId: The ID of the recipe to add to favorites.
 *        - onSuccess: Function to execute upon successful add favorite operation.
 *        - onFailure: Function to execute upon add favorite operation failure.
 *        - onLoading: Function to execute while add favorite operation is ongoing.
 * @param onRemoveFavoriteClicked [Function() -> Unit)]: Callback function for removing a recipe from favorites.
 *     - Takes the same arguments as `onFavoriteClicked`.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun SearchRecipeGrid(
    loggedUserId: Int,
    searchResultList: List<Recipe>,
    decodeImageToBitmap: (String) -> Bitmap?,
    onLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
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
                loggedUserId = loggedUserId,
                onLikeClicked = onLikeClicked,
                onRemoveLikeClicked = onRemoveLikeClicked,
                onFavoriteClicked = onFavoriteClicked,
                onRemoveFavoriteClicked = onRemoveFavoriteClicked,
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
 * @param loggedUserId [Int]: The ID of the currently logged-in user (used for like/unlike functionality).
 * @param onLikeClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for liking a recipe.
 *     - Takes:
 *        - loggedUserId: The ID of the logged-in user.
 *        - recipeId: The ID of the recipe to like.
 *        - onSuccess: Function to execute upon successful like operation.
 *        - onFailure: Function to execute upon like operation failure.
 *        - onLoading: Function to execute while the like operation is ongoing.
 * @param onRemoveLikeClicked [Function() -> Unit)]: Callback function for removing a like from a recipe.
 *      - Takes the same arguments as `onLikeClicked`.
 * @param onFavoriteClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for adding a recipe to favorites.
 *     - Takes the same arguments as in `SearchRecipeGrid`.
 * @param onRemoveFavoriteClicked [Function() -> Unit)]: Callback function for removing a recipe from favorites.
 *     - Takes the same arguments as in `SearchRecipeGrid`.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun SearchRecipeCard(
    recipe: Recipe,
    loggedUserId: Int,
    onLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var likeCount by remember { mutableIntStateOf(0) }
    var isLiked by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var isLikeResponseLoading by remember { mutableStateOf(false) } // Track loading state
    var isFavoriteResponseLoading by remember { mutableStateOf(false) } // Track loading state

    isLiked = recipe.hasLike
    likeCount = recipe.likeCount
    isFavorite = recipe.hasFavorite

    Surface(
        color = colorResource(id = R.color.md_theme_surfaceContainer)
    ) {
        Box(
            modifier = modifier.padding(top = dimensionResource(id = R.dimen.search_grid_top_padding))
        ) {
            // section for displaying recipe image
            Column(
                modifier = modifier
                    .width(dimensionResource(id = R.dimen.search_recipe_card_width))
                    .zIndex(1f)
                    .clickable { navigateToViewRecipe(recipe.idRecipe) },
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

            // favorite icon placed on top of the image for registered users
            if (loggedUserId != UserDefaultConstant.GUEST_USER_ID) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(dimensionResource(id = R.dimen.search_favorite_icon_box_size))
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_favorite_icon_box_corner_radius)))
                        .background(colorResource(id = R.color.md_theme_outlineVariant))
                        .zIndex(2f),
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = colorResource(id = R.color.md_theme_onPrimaryContainer),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.search_favorite_icon_size))
                            .align(Alignment.Center)
                            .clickable {
                                // Only allow favorite operation when not loading
                                if (!isFavoriteResponseLoading) {
                                    isFavorite = !isFavorite
                                    if (isFavorite) {
                                        // Call to favorite a recipe
                                        onFavoriteClicked(
                                            loggedUserId,
                                            recipe.idRecipe,
                                            {
                                                // Reset loading state on success
                                                isFavoriteResponseLoading = false
                                            },
                                            {
                                                // Reset loading state on failure
                                                isFavoriteResponseLoading = false

                                                // Revert the favorite status on failure
                                                isFavorite = !isFavorite
                                            },
                                            {
                                                // Set loading state on loading
                                                isFavoriteResponseLoading = true
                                            }
                                        )
                                    } else {
                                        // Call to remove a favorite
                                        onRemoveFavoriteClicked(
                                            loggedUserId,
                                            recipe.idRecipe,
                                            {
                                                // Reset loading state on success
                                                isFavoriteResponseLoading = false
                                            },
                                            {
                                                // Reset loading state on failure
                                                isFavoriteResponseLoading = false

                                                // Revert the favorite status on failure
                                                isFavorite = !isFavorite
                                            },
                                            {
                                                // Set loading state on loading
                                                isFavoriteResponseLoading = true
                                            }
                                        )
                                    }
                                }
                            }
                    )
                }
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
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable { navigateToViewRecipe(recipe.idRecipe) },
                    ) {
                        Text(
                            text = recipe.recipeName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.md_theme_onSurface),
                            overflow = TextOverflow.Ellipsis,
                            modifier = modifier.height(dimensionResource(id = R.dimen.search_recipe_card_name_label_height))
                        )
                    }

                    //section for displaying recipe calorie
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable { navigateToViewRecipe(recipe.idRecipe) },
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
                        if (loggedUserId == UserDefaultConstant.GUEST_USER_ID) {
                            // Does not allow logged in users to like recipes
                            Icon(
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                tint = colorResource(id = R.color.md_theme_outline_mediumContrast),
                                modifier = Modifier.size(dimensionResource(id = R.dimen.search_like_icon_size))
                            )
                        } else {
                            // Allow logged in users to like recipes
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                tint = colorResource(id = R.color.md_theme_primary),
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.search_like_icon_size))
                                    .clickable {
                                        // Only allow liking when not loading
                                        if (!isLikeResponseLoading) {
                                            isLiked = !isLiked
                                            if (isLiked) {
                                                // Call to like a recipe
                                                onLikeClicked(
                                                    loggedUserId,
                                                    recipe.idRecipe,
                                                    {
                                                        // Reset loading state on success
                                                        isLikeResponseLoading = false
                                                    },
                                                    {
                                                        // Reset loading state on failure
                                                        isLikeResponseLoading = false

                                                        // Revert the like status and like count on failure
                                                        isLiked = !isLiked
                                                        --likeCount
                                                    },
                                                    {
                                                        // Set loading state on loading
                                                        isLikeResponseLoading = true
                                                    }
                                                )
                                                // Increment like count
                                                ++likeCount
                                            } else {
                                                // Call to remove a like from a recipe
                                                onRemoveLikeClicked(
                                                    loggedUserId,
                                                    recipe.idRecipe,
                                                    {
                                                        // Reset loading state on success
                                                        isLikeResponseLoading = false
                                                    },
                                                    {
                                                        // Reset loading state on failure
                                                        isLikeResponseLoading = false

                                                        // Revert the like status and like count on failure
                                                        isLiked = !isLiked
                                                        ++likeCount
                                                    },
                                                    {
                                                        // Set loading state on loading
                                                        isLikeResponseLoading = true
                                                    }
                                                )
                                                // Decrement like count
                                                --likeCount
                                            }
                                        }
                                    }
                            )
                        }

                        Text(
                            text = stringResource(R.string.like_count, likeCount),
                            style = MaterialTheme.typography.bodySmall,
                            color = colorResource(id = R.color.md_theme_onSurface),
                        )
                    }
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
 * @param searchResultRecipeListState The state representing the fetched recipe list.
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
            navigateToViewRecipe = { null },
            categoryId = 1
        )
    }
}