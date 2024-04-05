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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.zIndex
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.data.RecipeListTestData
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function to display the section of recipe suggestions based on calorie.
 *
 * @param calorieBasedRecipeList List<Recipe>: The list of recipes to display in this section. Default is an empty list.
 * @param calorieFilterValue Int: The maximum calorie count used for filtering the recipes. Default is 300 kcal.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeSuggestionByCalorieSection(
    calorieBasedRecipeList: List<Recipe> = emptyList(),
    calorieFilterValue: Int,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.home_suggestion_bycalorie_title, calorieFilterValue)
    Column {
        RecipeSuggestionByCalorieTitle(title = title)
        RecipeSuggestionByCalorieGrid(
            calorieBasedRecipeList = calorieBasedRecipeList,
            decodeImageToBitmap = decodeImageToBitmap,
            navigateToViewRecipe = navigateToViewRecipe
        )
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
 * @param calorieBasedRecipeList List<Recipe>: The list of recipes to display in this section.
 * @param decodeImageToBitmap Function: Function to decode recipe images to Bitmap.
 * @param navigateToViewRecipe Function: Callback to navigate to the recipe details screen.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeSuggestionByCalorieGrid(
    calorieBasedRecipeList: List<Recipe>,
    decodeImageToBitmap: (String) -> Bitmap?,
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
fun RecipeByCalorieCard(
    recipe: Recipe,
    decodeImageToBitmap: (String) -> Bitmap?,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clickable {
            navigateToViewRecipe(recipe.idRecipe)
        }
    ) {
        // section for displaying recipe image
        Column(
            modifier = modifier
                .width(dimensionResource(id = R.dimen.home_small_suggestion_card_width))
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

        // favorite icon placed on top of the image
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(dimensionResource(id = R.dimen.home_favorite_icon_box_size))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.home_favorite_icon_box_corner_radius)))
                .background(colorResource(id = R.color.md_theme_outlineVariant))
                .zIndex(2f),
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
                    modifier = modifier.height(dimensionResource(id = R.dimen.home_recipe_name_label_height))

                )

                //section for displaying recipe calorie
                Row {
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
                        color = colorResource(id = R.color.md_theme_onSurface),
                    )
                }
            }
        }
    }
}

/**
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewRecipeSuggestionByCalorieSection() {
    val recipeListTestData = RecipeListTestData.getRecipeList()
    MaterialTheme {
        RecipeSuggestionByCalorieGrid(
            calorieBasedRecipeList = recipeListTestData,
            decodeImageToBitmap = { null },
            navigateToViewRecipe = {}
        )
    }
}