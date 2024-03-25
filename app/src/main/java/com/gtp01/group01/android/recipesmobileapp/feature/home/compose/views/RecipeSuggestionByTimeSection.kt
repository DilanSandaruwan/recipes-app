package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.data.RecipeListTestData
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function to display the section of recipe suggestions based on time.
 *
 * @param timeBasedRecipeList List<Recipe>: The list of recipes to display in this section. Default is an empty list.
 * @param filterByTime Int: The time duration used for filtering the recipes. Default is 30 min.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeSuggestionByTimeSection(
    timeBasedRecipeList: List<Recipe> = emptyList(),
    filterByTime: Int,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.home_suggestion_bytime_title, filterByTime)
    Column {
        RecipeSuggestionByTimeTitle(title)
        RecipeSuggestionByTimeGrid(
            timeBasedRecipeList = timeBasedRecipeList,
            navigateToViewRecipe = navigateToViewRecipe
        )
    }
}

/**
 * Composable function to display the grid of recipe suggestions based on time.
 *
 * @param timeBasedRecipeList List<Recipe>: The list of recipes to display in this section.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeSuggestionByTimeGrid(
    timeBasedRecipeList: List<Recipe>,
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
                navigateToViewRecipe = navigateToViewRecipe
            )
        }
    }
}

/**
 * Composable function to display a single recipe card.
 *
 * @param recipe Recipe: The recipe to display.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun RecipeByTimeCard(
    recipe: Recipe,
    navigateToViewRecipe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = colorResource(id = R.color.md_theme_inverseOnSurface),
    ) {
        Column(modifier = modifier.clickable {
            navigateToViewRecipe(recipe.idRecipe)
        }) {
            Image(
                painter = painterResource(R.drawable.img_burger_meal_with_french_fries),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.size(
                    dimensionResource(id = R.dimen.large_suggestion_card_view_width),
                    dimensionResource(id = R.dimen.large_suggestion_card_view_img_height)
                )
            )
        Column {
            recipe.photo?.let {
                val bitmap: Bitmap? = decodeBase64ToBitmap(recipe.photo)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .size(
                            dimensionResource(id = R.dimen.home_large_suggestion_card_width),
                            dimensionResource(id = R.dimen.home_large_suggestion_card_img_height))
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                    )
                }?:run{
                    Image(
                        painter = painterResource(R.drawable.img_burger_meal_with_french_fries),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier.size(
                            dimensionResource(id = R.dimen.home_large_suggestion_card_width),
                            dimensionResource(id = R.dimen.home_large_suggestion_card_img_height)
                        )
                    )
                }
            }?:run{
                Image(
                    painter = painterResource(R.drawable.img_burger_meal_with_french_fries),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier.size(
                        dimensionResource(id = R.dimen.home_large_suggestion_card_width),
                        dimensionResource(id = R.dimen.home_large_suggestion_card_img_height)
                    )
                )
            }


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.Top,
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
                Row(
                    modifier = modifier
//                    .height(dimensionResource(id = R.dimen.home_likes_label_height))
                        .padding(vertical = dimensionResource(id = R.dimen.home_likes_label_padding))
                ) {
                    Text(
                        text = stringResource(id = R.string.likes),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(id = R.color.md_theme_outline_mediumContrast),
//                        modifier = modifier
//                            .height(dimensionResource(id = R.dimen.home_likes_label_height))
//                            .padding(vertical = dimensionResource(id = R.dimen.home_likes_label_padding))
                    )
                    Text(
                        text = recipe.likeCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(id = R.color.md_theme_onSurface),
//                        modifier = modifier
//                            .height(dimensionResource(id = R.dimen.home_likes_label_height))
//                            .padding(vertical = dimensionResource(id = R.dimen.home_likes_label_padding))
                    )
                }
            }
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

fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedBytes: ByteArray =
        android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
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
            filterByTime = 30,
            navigateToViewRecipe = {})
    }
}