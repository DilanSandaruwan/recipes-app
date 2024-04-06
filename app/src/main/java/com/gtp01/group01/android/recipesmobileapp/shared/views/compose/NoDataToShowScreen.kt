package com.gtp01.group01.android.recipesmobileapp.shared.views.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.gtp01.group01.android.recipesmobileapp.R

@Composable
fun NoDataToShowScreen(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
                .padding(top = dimensionResource(id = R.dimen.no_data_text_padding_top))
                .align(Alignment.CenterHorizontally)
        )
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(
                    width = dimensionResource(id = R.dimen.no_data_img_width),
                    height = dimensionResource(id = R.dimen.no_data_img_height)
                )
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.list_card_view_corner_radius)))
                .align(Alignment.CenterHorizontally)
        )
    }
}