package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.gtp01.group01.android.recipesmobileapp.R

/**
 * Composable function to display the search bar section.
 *
 * @param searchKeyword String: The current search keyword entered by the user.
 * @param onSearchKeywordChange Function: Callback triggered when the search keyword changes.
 * @param isValidKeyword Function: Check validity of entered text when the keyboard's "Search" action is pressed.
 * @param onKeyboardSearch Function: Callback triggered when the keyboard's "Search" action is pressed.
 * @param onClearButtonClicked Function: Callback triggered when the clear button is pressed.
 * @param modifier Modifier: The modifier for styling the layout. Default is [Modifier].
 */
@Composable
fun SearchBarSection(
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
    isValidKeyword: (String) -> Boolean,
    onKeyboardSearch: (String) -> Unit,
    onClearButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.md_theme_surfaceContainer)
        ),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.home_search_bar_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.home_search_bar_corner_elevation))
    ) {
        OutlinedTextField(
            value = searchKeyword,
            onValueChange = onSearchKeywordChange,
            singleLine = true,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.home_search_bar_corner_radius)),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (searchKeyword.isNotBlank()) {
                    ClearButton(onClearButtonClicked = onClearButtonClicked)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            placeholder = {
                Text(stringResource(R.string.placeholder_search))
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (isValidKeyword(searchKeyword)) {
                    onKeyboardSearch(searchKeyword)
                }
            }),
            modifier = modifier.fillMaxWidth()
        )
    }
}

/**
 * Composable function for the clear button.
 *
 * @param onClearButtonClicked Callback function for the clear button click.
 */
@Composable
fun ClearButton(onClearButtonClicked: () -> Unit) {
    IconButton(
        onClick = { onClearButtonClicked() },
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null
        )
    }
}

/**
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewSearchBarSection() {
    MaterialTheme {
        SearchBarSection(
            onKeyboardSearch = {},
            searchKeyword = "",
            isValidKeyword = { false },
            onSearchKeywordChange = {},
            onClearButtonClicked = {}
        )
    }
}