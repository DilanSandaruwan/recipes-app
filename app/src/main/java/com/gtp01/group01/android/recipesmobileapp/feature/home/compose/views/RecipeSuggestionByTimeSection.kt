package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe

/**
 * Composable function to display the section of recipe suggestions based on time.
 *
 * @param timeBasedRecipeList List<Recipe>: The list of recipes to display in this section. Default is an empty list.
 */
@Composable
fun RecipeSuggestionByTimeSection(timeBasedRecipeList: List<Recipe> = emptyList()) {
    RecipeSuggestionByTimeTitle()
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding))) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding)),
            contentPadding = PaddingValues(dimensionResource(R.dimen.padding)),
            modifier = Modifier
                .padding(vertical = dimensionResource(R.dimen.padding))
        ) {
            items(timeBasedRecipeList) {
                Text(text = it.recipeName)
            }
        }
    }
}

/**
 * Composable function to display the section title for recipe suggestion based on the specified time duration.
 *
 * @param timeInMinutes Int: The duration in minutes for the recipe suggestion. Default is 30 minutes.
 */
@Composable
fun RecipeSuggestionByTimeTitle(timeInMinutes: Int = 30) {
    Column {
        val context = LocalContext.current
        val formattedString = remember(timeInMinutes) {
            context.getString(R.string.home_suggestion_bytime_title, timeInMinutes)
        }
        Text(text = formattedString)
    }
}

/**
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewRecipeSuggestionTitle() {
    MaterialTheme {
        RecipeSuggestionByTimeTitle()
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewHomeScreenContent() {
    MaterialTheme {
        RecipeSuggestionByTimeSection()
    }
}