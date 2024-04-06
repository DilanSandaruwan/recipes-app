package com.gtp01.group01.android.recipesmobileapp.feature.my_recipes.compose

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.feature.my_recipes.MyRecipesViewModel
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable screen displaying a list of recipes created by the current user.
 *
 * @param myRecipesViewModel MyRecipesViewModel: ViewModel providing access to recipe data and actions.
 * @param navigateToUpdateRecipe Function: Callback to navigate to the recipe update screen.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 */
@Composable
fun MyRecipesScreen(
    myRecipesViewModel: MyRecipesViewModel = hiltViewModel(),
    navigateToUpdateRecipe: (Int) -> Unit,
    navigateToViewRecipe: (Int) -> Unit
) {
    // Initiate retrieval of recipes from the ViewModel
    myRecipesViewModel.getMyRecipes(10)
    // Observe the recipe list state and current user information
    val myRecipesListState = myRecipesViewModel.myRecipesList.collectAsState()
    val user by myRecipesViewModel.user.observeAsState(0)
    // Surface container for the screen content
    Surface(
        color = colorResource(id = R.color.md_theme_surfaceContainer),
        shape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.search_screen_corner_radius),
            topEnd = dimensionResource(id = R.dimen.search_screen_corner_radius),
        ),
        modifier = Modifier
            .padding(
                top = dimensionResource(id = R.dimen.search_screen_top_padding),
                bottom = dimensionResource(id = R.dimen.search_screen_bottom_padding)
            )
            .fillMaxSize()
    ) {
        // Display the grid of recipes if successful retrieval
        if (myRecipesListState.value is Result.Success) {
            val recipeResult = myRecipesListState.value as Result.Success<List<Recipe>>
            MyRecipesGrid(
                searchResultList = recipeResult.result,
                decodeImageToBitmap = { myRecipesViewModel.decodeImageToBitmap(it) },
                navigateToViewRecipe = navigateToViewRecipe,
                navigateToUpdateRecipe = navigateToUpdateRecipe
            )
        }
    }
}

/**
 * Composable function to display a grid of recipe cards.
 *
 * @param searchResultList List<Recipe>: The list of recipes to display.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param navigateToUpdateRecipe Function: Callback to navigate to the recipe update screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun MyRecipesGrid(
    searchResultList: List<Recipe>,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    navigateToUpdateRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Lazy grid for efficient rendering of a large number of recipe cards
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.content_horizontal_margin)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.activity_vertical_margin))
            .fillMaxHeight()
    ) {
        items(searchResultList) {
            MyRecipeCardInGrid(
                recipe = it,
                decodeImageToBitmap = decodeImageToBitmap,
                navigateToViewRecipe = navigateToViewRecipe,
                navigateToUpdateRecipe = navigateToUpdateRecipe
            )
        }
    }
}

/**
 * Composable function to display a single recipe card based on calorie information.
 *
 * @param recipe Recipe: The recipe data to be displayed on the card.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param navigateToUpdateRecipe Function: Callback to navigate to the recipe update screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun MyRecipeCardInGrid(
    recipe: Recipe,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    navigateToUpdateRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = dimensionResource(id = R.dimen.search_grid_top_padding))
    ) {
        // Section for displaying recipe image
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

        // Edit icon positioned on top of the image
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(dimensionResource(id = R.dimen.search_favorite_icon_box_size))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_favorite_icon_box_corner_radius)))
                .background(colorResource(id = R.color.md_theme_outlineVariant))
                .zIndex(2f),
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = colorResource(id = R.color.md_theme_outline),
                modifier = Modifier
                    .clickable {
                        navigateToUpdateRecipe(recipe.idRecipe)
                    }
                    .size(dimensionResource(id = R.dimen.search_favorite_icon_size))
                    .align(Alignment.Center)
            )
        }

        // Section displaying recipe details
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
                // Section for displaying recipe name
                Text(
                    text = recipe.recipeName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.md_theme_onSurface),
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.height(dimensionResource(id = R.dimen.search_recipe_card_name_label_height))

                )

                // Section for displaying recipe calorie
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

                // Section for displaying recipe likes
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
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
                    Spacer(modifier = Modifier.weight(1f)) // Spacer to push the Icon to the end

                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = colorResource(id = R.color.md_theme_outline),
                        modifier = Modifier
                            .clickable {
                                navigateToViewRecipe(recipe.idRecipe)
                            }
                            .size(dimensionResource(id = R.dimen.size_info_icon))
                    )

                }
            }
        }
    }
}
