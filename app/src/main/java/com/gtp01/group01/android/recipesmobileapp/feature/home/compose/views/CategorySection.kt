package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.gtp01.group01.android.recipesmobileapp.R

/**
 * Displays a section for food categories, allowing users to filter content based on their selection.
 *
 * @param onFilterByCategory A callback function that will be invoked when a category is selected.
 */
@Composable
fun CategorySection(
    onFilterByCategory: (Int) -> Unit
) {
    // List of food categories with their corresponding image resources and string resource IDs for category name labels
    val foodCategories = listOf(
        Pair(R.drawable.fc_1, R.string.bread),
        Pair(R.drawable.fc_2, R.string.breakfast),
        Pair(R.drawable.fc_3, R.string.dessert),
        Pair(R.drawable.fc_4, R.string.dinner),
        Pair(R.drawable.fc_5, R.string.drinks),
        Pair(R.drawable.fc_6, R.string.lunch),
        Pair(R.drawable.fc_7, R.string.meat),
        Pair(R.drawable.fc_8, R.string.noodles),
        Pair(R.drawable.fc_9, R.string.pizza),
        Pair(R.drawable.fc_10, R.string.rice),
        Pair(R.drawable.fc_11, R.string.salad),
        Pair(R.drawable.fc_12, R.string.sides),
        Pair(R.drawable.fc_13, R.string.soup),
        Pair(R.drawable.fc_14, R.string.vegetarian)
    )
    CategoryList(
        foodCategories = foodCategories,
        onFilterByCategory = onFilterByCategory
    )
}

/**
 * Composes a horizontally scrollable list of food categories, where each category is displayed with an image and text.
 *
 * @param foodCategories List<Pair<Int, Int>>: A list of food categories where each item is a pair of:
 *      - Image drawable resource ID for the category's icon.
 *      - String resource ID for the category's name label.
 * @param onFilterByCategory (Int) -> Unit: A callback function invoked when a category is selected.
 */
@Composable
fun CategoryList(
    foodCategories: List<Pair<Int, Int>>,
    onFilterByCategory: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.home_category_space_between)),
        content = {
            items(foodCategories) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        onFilterByCategory(foodCategories.indexOf(it) + 1)
                    }
                ) {
                    ImageItem(imageRes = it.first)
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.home_category_space)))
                    CategoryName(categoryName = it.second)
                }
            }
        }
    )
}

/**
 * Composable function to display an image item representing a food category.
 *
 * @param imageRes Int: The resource ID of the image representing the food category.
 */
@Composable
fun ImageItem(imageRes: Int) {
    Surface(
        color = colorResource(id = R.color.md_theme_onPrimary),
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.home_category_card_corner_radius)))
            .size(
                width = dimensionResource(id = R.dimen.home_category_item_width),
                height = dimensionResource(id = R.dimen.home_category_item_height)
            )
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.home_category_space))
                .size(
                    width = dimensionResource(id = R.dimen.home_category_item_img_width),
                    height = dimensionResource(id = R.dimen.home_category_item_img_height)
                )
        )
    }
}

/**
 * Composable function to display the name of a food category.
 *
 * @param categoryName Int: The string resource ID representing the name of the food category.
 */
@Composable
fun CategoryName(categoryName: Int) {
    Text(
        text = stringResource(id = categoryName),
        style = MaterialTheme.typography.bodyMedium,
        color = colorResource(id = R.color.md_theme_onSurface),
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewCategorySection() {
    MaterialTheme {
        CategorySection(onFilterByCategory = { null })
    }
}