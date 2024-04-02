package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantResponseCode

/**
 * Composable function for displaying an error message.
 *
 * @param errorCode The error code to determine the error message.
 * @param modifier Optional modifier for styling.
 * @param onRetry Callback function to retry the operation.
 */
@Composable
fun ShowError(
    errorCode: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            Text(text = stringResource(id = getErrorMessageForCode(errorCode)))
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            Button(onClick = onRetry) {
                Text(stringResource(id = R.string.retry))
            }
        }
    }
}

/**
 * Function to retrieve localized error message for the provided error code.
 *
 * @param errorCode The error code to determine the error message.
 * @return The resource ID of the corresponding error message.
 */
@Composable
fun getErrorMessageForCode(errorCode: String): Int {
    return when (errorCode) {
        ConstantResponseCode.SERVER_NOT_FOUND.toString() -> R.string.home_error_404
        ConstantResponseCode.INTERNAL_SERVER_ERROR.toString() -> R.string.home_error_500
        ConstantResponseCode.IOEXCEPTION -> R.string.home_error_ioexception
        else -> R.string.home_error_exception
    }
}