package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.home_suggestion_bytime_title, filterByTime)
    Column {
        RecipeSuggestionByTimeTitle(title)
        RecipeSuggestionByTimeGrid(timeBasedRecipeList)
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
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.content_horizontal_margin)),
        modifier = modifier
            .height(dimensionResource(id = R.dimen.large_suggestion_card_view_max_height))
    ) {
        items(timeBasedRecipeList) {
            RecipeByTimeCard(recipe = it)
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
    modifier: Modifier = Modifier
) {
    Surface(
        color = colorResource(id = R.color.md_theme_surface),
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.img_burger_meal_with_french_fries),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.size(
                    dimensionResource(id = R.dimen.large_suggestion_card_view_width),
                    dimensionResource(id = R.dimen.large_suggestion_card_view_img_height)
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = modifier
                    .width(dimensionResource(id = R.dimen.large_suggestion_card_view_width))
                    .padding(vertical = dimensionResource(id = R.dimen.padding))
            ) {
                Text(
                    text = recipe.recipeName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.md_theme_onSurface),
                    modifier = modifier.width(dimensionResource(id = R.dimen.large_suggestion_card_view_name_width))
                )
                Row {
                    Text(
                        text = stringResource(id = R.string.likes) + " ",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(id = R.color.md_theme_outline_mediumContrast)
                    )
                    Text(
                        text = recipe.likeCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(id = R.color.md_theme_onSurface),
                    )
                }
            }
            Row {
                Text(
                    text = recipe.preparationTime.toString() + " " + stringResource(id = R.string.min),
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

/**
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewRecipeSuggestionByTimeSection() {
    val recipeListTestData = RecipeListTestData.getRecipeList()
    MaterialTheme {
        RecipeSuggestionByTimeSection(timeBasedRecipeList = recipeListTestData, filterByTime = 30)
    }
}