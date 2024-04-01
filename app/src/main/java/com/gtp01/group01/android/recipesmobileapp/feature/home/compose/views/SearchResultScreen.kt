package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SearchResultScreen(recipeName: String) {
    Text(text = recipeName)
}