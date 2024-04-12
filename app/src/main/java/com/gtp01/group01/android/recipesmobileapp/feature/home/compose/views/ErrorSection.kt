package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.activity_horizontal_margin)))
            Text(
                text = stringResource(id = getErrorMessageForCode(errorCode)),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.margin_top)))
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
        ConstantResponseCode.BAD_REQUEST.toString() -> R.string.error_400
        ConstantResponseCode.SERVER_NOT_FOUND.toString() -> R.string.error_404
        ConstantResponseCode.REQUEST_TIMEOUT.toString() -> R.string.error_408
        ConstantResponseCode.INTERNAL_SERVER_ERROR.toString() -> R.string.error_500
        ConstantResponseCode.BAD_GATEWAY.toString() -> R.string.error_502
        ConstantResponseCode.SERVICE_UNAVAILABLE.toString() -> R.string.error_503
        ConstantResponseCode.GATEWAY_TIMEOUT.toString() -> R.string.error_504
        ConstantResponseCode.IOEXCEPTION -> R.string.error_ioexception
        ConstantResponseCode.TIMEOUT_EXCEPTION -> R.string.error_connection_timeout
        else -> R.string.generic_error_exception
    }
}

/**
 * Below are preview composable functions.
 * These functions are intended for use in a preview environment during development.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewShowError() {
    MaterialTheme {
        ShowError(
            errorCode = "Exception",
            onRetry = {}
        )
    }
}