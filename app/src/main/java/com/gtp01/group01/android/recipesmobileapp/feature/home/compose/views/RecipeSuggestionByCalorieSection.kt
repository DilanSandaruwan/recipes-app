package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.UserDefaultConstant.GUEST_USER_ID
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function to display a section of recipe suggestions based on preferred calorie count.
 *
 * This composable displays recipes filtered by the user's preferred calorie count (`calorieFilterValue`).
 * It retrieves data from the `calorieBasedRecipeListState` and presents the recipe suggestions with:
 *  - Titles
 *  - Images (decoded using `decodeImageToBitmap`)
 *  - Like/Unlike functionality (`onLikeClicked` and `onRemoveLikeClicked`) for user interaction.
 *  - Navigation to the recipe details screen (`navigateToViewRecipe`) on recipe item click.
 *
 * @param calorieBasedRecipeListState [State<Result<List<Recipe>>>]: Represents the state of calorie-based recipe list data.
 *     - Result.Success: Contains a list of recipes filtered by calorie count.
 *     - Result.Error: Indicates an error occurred during data fetching.
 * @param loggedUserId [Int]: The ID of the currently logged-in user (used for like/unlike functionality).
 * @param calorieFilterValue [Int]: The preferred calorie count used for filtering recipes (in kCal).
 * @param decodeImageToBitmap [Function<String, Bitmap?>]: Function to decode recipe image URLs into Bitmap objects.
 * @param onLikeClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for liking a recipe.
 *     - Takes:
 *        - loggedUserId: The ID of the logged-in user.
 *        - recipeId: The ID of the recipe to like.
 *        - onSuccess: Function to execute upon successful like operation.
 *        - onFailure: Function to execute upon like operation failure.
 *        - onLoading: Function to execute while the like operation is ongoing.
 * @param onRemoveLikeClicked [Function() -> Unit)]: Callback function for removing a like from a recipe.
 *     - Takes the same arguments as `onLikeClicked`.
 * @param onFavoriteClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for adding a recipe to favorites.
 *     - Takes:
 *        - loggedUserId: The ID of the logged-in user.
 *        - recipeId: The ID of the recipe to add to favorites.
 *        - onSuccess: Function to execute upon successful add favorite operation.
 *        - onFailure: Function to execute upon add favorite operation failure.
 *        - onLoading: Function to execute while add favorite operation is ongoing.
 * @param onRemoveFavoriteClicked [Function() -> Unit)]: Callback function for removing a recipe from favorites.
 *     - Takes the same arguments as `onFavoriteClicked`.
 * @param onSuccessfulRecipeUpdate [Function() -> Unit)]: Function to trigger fetching recipes with updated data for 'RecipeSuggestionByTimeSection'.
 * @param navigateToViewRecipe [Function<Int, Unit>]: Callback function to navigate to the recipe details screen.
 *     - Takes the recipe ID as an argument.
 * @param modifier [Modifier]: Optional modifier for styling the layout (defaults to Modifier).
 */
@Composable
fun RecipeSuggestionByCalorieSection(
    calorieBasedRecipeListState: State<Result<List<Recipe>>>,
    loggedUserId: Int,
    calorieFilterValue: Int,
    decodeImageToBitmap: (String) -> Bitmap?,
    onLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onSuccessfulRecipeUpdate: () -> Unit,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.home_suggestion_bycalorie_title, calorieFilterValue)
    Column {
        RecipeSuggestionByCalorieTitle(title)
        if (calorieBasedRecipeListState.value is Result.Success) {
            val recipeResult =
                calorieBasedRecipeListState.value as Result.Success<List<Recipe>>
            RecipeSuggestionByCalorieGrid(
                loggedUserId = loggedUserId,
                calorieBasedRecipeList = recipeResult.result,
                decodeImageToBitmap = decodeImageToBitmap,
                onLikeClicked = onLikeClicked,
                onRemoveLikeClicked = onRemoveLikeClicked,
                onFavoriteClicked = onFavoriteClicked,
                onRemoveFavoriteClicked = onRemoveFavoriteClicked,
                onSuccessfulRecipeUpdate = onSuccessfulRecipeUpdate,
                navigateToViewRecipe = navigateToViewRecipe,
                modifier = modifier
            )
        }
    }
}

/**
 * Composable function to display the section title for recipe suggestion based on the specified calorie count.
 *
 * @param title String: The title to display.
 */
@Composable
fun RecipeSuggestionByCalorieTitle(title: String) {
    Column(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.editText_height))
            .padding(vertical = dimensionResource(id = R.dimen.padding))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Composable function to display the grid of recipe suggestions based on calorie.
 *
 * @param loggedUserId [Int]: The ID of the currently logged-in user (used for like/unlike functionality).
 * @param calorieBasedRecipeList List<Recipe>: The list of recipes to display in this section.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param onLikeClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for liking a recipe.
 *     - Takes the same arguments as in `RecipeSuggestionByCalorieSection`.
 * @param onRemoveLikeClicked [Function() -> Unit)]: Callback function for removing a like from a recipe.
 *     - Takes the same arguments as in `RecipeSuggestionByCalorieSection`.
 * @param onFavoriteClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for adding a recipe to favorites.
 *     - Takes the same arguments as in `RecipeSuggestionByCalorieSection`.
 * @param onRemoveFavoriteClicked [Function() -> Unit)]: Callback function for removing a recipe from favorites.
 *     - Takes the same arguments as in `RecipeSuggestionByCalorieSection`.
 * @param onSuccessfulRecipeUpdate [Function() -> Unit)]: Function to trigger fetching recipes with updated data for 'RecipeSuggestionByTimeSection'.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeSuggestionByCalorieGrid(
    loggedUserId: Int,
    calorieBasedRecipeList: List<Recipe>,
    decodeImageToBitmap: (String) -> Bitmap?,
    onLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onSuccessfulRecipeUpdate: () -> Unit,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.content_horizontal_margin)),
        modifier = modifier
            .height(dimensionResource(id = R.dimen.home_small_suggestion_card_height))
    ) {
        items(calorieBasedRecipeList) {
            RecipeByCalorieCard(
                recipe = it,
                loggedUserId = loggedUserId,
                decodeImageToBitmap = decodeImageToBitmap,
                onLikeClicked = onLikeClicked,
                onRemoveLikeClicked = onRemoveLikeClicked,
                onFavoriteClicked = onFavoriteClicked,
                onRemoveFavoriteClicked = onRemoveFavoriteClicked,
                onSuccessfulRecipeUpdate = onSuccessfulRecipeUpdate,
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
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param onLikeClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for liking a recipe.
 *     - Takes the same arguments as in `RecipeSuggestionByCalorieSection`.
 * @param onRemoveLikeClicked [Function() -> Unit)]: Callback function for removing a like from a recipe.
 *     - Takes the same arguments as in `RecipeSuggestionByCalorieSection`.
 * @param onFavoriteClicked [Function<Int, Int, () -> Unit, (String) -> Unit, () -> Unit>]: Callback function for adding a recipe to favorites.
 *     - Takes the same arguments as in `RecipeSuggestionByCalorieSection`.
 * @param onRemoveFavoriteClicked [Function() -> Unit)]: Callback function for removing a recipe from favorites.
 *     - Takes the same arguments as in `RecipeSuggestionByCalorieSection`.
 * @param onSuccessfulRecipeUpdate [Function() -> Unit)]: Function to trigger fetching recipes with updated data for 'RecipeSuggestionByTimeSection'.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeByCalorieCard(
    recipe: Recipe,
    loggedUserId: Int,
    decodeImageToBitmap: (String) -> Bitmap?,
    onLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveLikeClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onRemoveFavoriteClicked: (loggedUserId: Int, recipeId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit, onLoading: () -> Unit) -> Unit,
    onSuccessfulRecipeUpdate: () -> Unit,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var likeCount by remember { mutableIntStateOf(0) }
    var isLiked by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var isLikeResponseLoading by remember { mutableStateOf(false) } // Track like operation loading state
    var isFavoriteResponseLoading by remember { mutableStateOf(false) } // Track favorite operation loading state

    isLiked = recipe.hasLike
    likeCount = recipe.likeCount
    isFavorite = recipe.hasFavorite

    Surface(
        color = colorResource(id = R.color.md_theme_surfaceContainer)
    ) {
        Box {
            // section for displaying recipe image
            Column(
                modifier = modifier
                    .width(dimensionResource(id = R.dimen.home_small_suggestion_card_width))
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
                                    width = dimensionResource(id = R.dimen.home_small_suggestion_card_img_width),
                                    height = dimensionResource(id = R.dimen.home_small_suggestion_card_img_height)
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
                                    width = dimensionResource(id = R.dimen.home_small_suggestion_card_img_width),
                                    height = dimensionResource(id = R.dimen.home_small_suggestion_card_img_height)
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
                                width = dimensionResource(id = R.dimen.home_small_suggestion_card_img_width),
                                height = dimensionResource(id = R.dimen.home_small_suggestion_card_img_height)
                            )
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))
                    )
                }
            }

            // favorite icon placed on top of the image for registered users
            if (loggedUserId != GUEST_USER_ID) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(dimensionResource(id = R.dimen.home_favorite_icon_box_size))
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.home_favorite_icon_box_corner_radius)))
                        .background(colorResource(id = R.color.md_theme_outlineVariant))
                        .zIndex(2f),
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = colorResource(id = R.color.md_theme_onPrimaryContainer),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.home_favorite_icon_size))
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
                                                // Trigger fetching updated data for time based suggestion section
                                                onSuccessfulRecipeUpdate()

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
                                                // Trigger fetching updated data for time based suggestion section
                                                onSuccessfulRecipeUpdate()

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
                    .width(dimensionResource(id = R.dimen.home_small_suggestion_card_width))
                    .padding(top = dimensionResource(id = R.dimen.home_small_suggestion_card_text_top_padding))
                    .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(colorResource(id = R.color.md_theme_onPrimary))
                        .padding(
                            top = dimensionResource(id = R.dimen.home_small_suggestion_card_text_top_padding),
                            bottom = dimensionResource(id = R.dimen.home_small_suggestion_card_text_bottom_padding)
                        )
                        .padding(horizontal = dimensionResource(id = R.dimen.home_small_suggestion_card_text_bottom_padding))
                        .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))
                ) {
                    //section for displaying recipe name
                    Text(
                        text = recipe.recipeName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.md_theme_onSurface),
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier
                            .height(dimensionResource(id = R.dimen.home_recipe_name_label_height))
                            .clickable { navigateToViewRecipe(recipe.idRecipe) },
                    )

                    //section for displaying recipe calorie
                    Row(
                        modifier = modifier.clickable { navigateToViewRecipe(recipe.idRecipe) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.calorie_kcal, recipe.calorie),
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.md_theme_outline_mediumContrast),
                            overflow = TextOverflow.Ellipsis,
                            modifier = modifier.height(dimensionResource(id = R.dimen.home_recipe_name_label_height))
                        )
                    }

                    //section for displaying recipe likes
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        if (loggedUserId == GUEST_USER_ID) {
                            // Does not allow guest users to like recipes
                            Icon(
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                tint = colorResource(id = R.color.md_theme_outline_mediumContrast),
                                modifier = Modifier.size(dimensionResource(id = R.dimen.home_like_icon_size))
                            )
                        } else {
                            // Allow logged in users to like recipes
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                tint = colorResource(id = R.color.md_theme_primary),
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.home_like_icon_size))
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
                                                        // Trigger fetching updated data for time based suggestion section
                                                        onSuccessfulRecipeUpdate()

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
                                                        // Trigger fetching updated data for time based suggestion section
                                                        onSuccessfulRecipeUpdate()

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