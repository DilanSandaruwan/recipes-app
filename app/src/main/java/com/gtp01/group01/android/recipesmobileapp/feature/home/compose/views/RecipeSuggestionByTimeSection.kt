package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
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
import androidx.compose.ui.tooling.preview.Preview
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.data.RecipeListTestData
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function to display the section of recipe suggestions based on time.
 *
 * @param timeBasedRecipeList List<Recipe>: The list of recipes to display in this section. Default is an empty list.
 * @param timeFilterValue Int: The time duration used for filtering the recipes. Default is 30 min.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeSuggestionByTimeSection(
    timeBasedRecipeList: List<Recipe> = emptyList(),
    timeFilterValue: Int,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.home_suggestion_bytime_title, timeFilterValue)
    Column {
        RecipeSuggestionByTimeTitle(title)
        RecipeSuggestionByTimeGrid(
            timeBasedRecipeList = timeBasedRecipeList,
            decodeImageToBitmap = decodeImageToBitmap,
            navigateToViewRecipe = navigateToViewRecipe
        )
    }
}

/**
 * Composable function to display the grid of recipe suggestions based on time.
 *
 * @param timeBasedRecipeList List<Recipe>: The list of recipes to display in this section.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeSuggestionByTimeGrid(
    timeBasedRecipeList: List<Recipe>,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.content_horizontal_margin)),
        modifier = modifier
            .height(dimensionResource(id = R.dimen.home_large_suggestion_card_height))
    ) {
        items(timeBasedRecipeList) {
            RecipeByTimeCard(
                recipe = it,
                decodeImageToBitmap = decodeImageToBitmap,
                navigateToViewRecipe = navigateToViewRecipe
            )
        }
    }
}

/**
 * Composable function to display a single recipe card.
 *
 * @param recipe Recipe: The recipe to display.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeByTimeCard(
    recipe: Recipe,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = colorResource(id = R.color.md_theme_surfaceContainer),
    ) {
        Column(
            modifier = modifier.clickable {
                navigateToViewRecipe(recipe.idRecipe)
            }
        ) {
            Column {
                // section for displaying recipe image and favorite icon
                Box {
                    // display recipe image
                    recipe.photo?.let {
                        val bitmap: Bitmap? = decodeImageToBitmap(recipe.photo.toString())
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .size(
                                        dimensionResource(id = R.dimen.home_large_suggestion_card_width),
                                        dimensionResource(id = R.dimen.home_large_suggestion_card_img_height)
                                    )
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.home_suggestion_card_corner_radius))),
                            )
                        } ?: run {
                            Image(
                                painter = painterResource(R.drawable.error_image),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = modifier.size(
                                    dimensionResource(id = R.dimen.home_large_suggestion_card_width),
                                    dimensionResource(id = R.dimen.home_large_suggestion_card_img_height)
                                )
                            )
                        }
                    } ?: run {
                        Image(
                            painter = painterResource(R.drawable.error_image),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = modifier.size(
                                dimensionResource(id = R.dimen.home_large_suggestion_card_width),
                                dimensionResource(id = R.dimen.home_large_suggestion_card_img_height)
                            )
                        )
                    }

                    // favorite icon placed on top of the image
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(
                                top = dimensionResource(id = R.dimen.home_favorite_icon_box_padding),
                                end = dimensionResource(id = R.dimen.home_favorite_icon_box_padding)
                            )
                            .size(dimensionResource(id = R.dimen.home_favorite_icon_box_size))
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.home_favorite_icon_box_corner_radius)))
                            .background(colorResource(id = R.color.md_theme_outlineVariant))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = colorResource(id = R.color.md_theme_outline),
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.home_favorite_icon_size))
                                .align(Alignment.Center)
                        )
                    }
                }

                //section for displaying recipe name and likes
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .width(dimensionResource(id = R.dimen.home_large_suggestion_card_width))
                        .padding(vertical = dimensionResource(id = R.dimen.home_label_padding))
                ) {
                    Text(
                        text = recipe.recipeName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.md_theme_onSurface),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.home_large_suggestion_card_name_width))
                            .height(dimensionResource(id = R.dimen.home_recipe_name_label_height))
                    )

                    //section for displaying recipe likes
                    Row(
                        modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.home_likes_label_padding)),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = null,
                            tint = colorResource(id = R.color.md_theme_outline_mediumContrast),
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.home_like_icon_size))
                        )
                        Text(
                            text = stringResource(R.string.like_count, recipe.likeCount),
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(id = R.color.md_theme_onSurface),
                        )
                    }
                }

                //section for displaying recipe preparation time
                Row {
                    Text(
                        text = stringResource(R.string.duration_min, recipe.preparationTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(id = R.color.md_theme_outline_mediumContrast),
                        fontWeight = FontWeight.Light,
                    )
                }
            }
        }
    }
}

/**
 * Composable function to display the section title for recipe suggestion based on the specified time duration.
 *
 * @param title String: The title to display.
 */
@Composable
fun RecipeSuggestionByTimeTitle(title: String) {
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
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewRecipeSuggestionByTimeSection() {
    val recipeListTestData = RecipeListTestData.getRecipeList()
    MaterialTheme {
        RecipeSuggestionByTimeSection(
            timeBasedRecipeList = recipeListTestData,
            timeFilterValue = 30,
            decodeImageToBitmap = { null },
            navigateToViewRecipe = {})
    }
}