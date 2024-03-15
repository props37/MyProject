package ru.zarina.zarina.ui.common.base

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import ru.zarina.zarina.R
import java.io.IOException

data class ErrorState(
    @DrawableRes
    val iconResId: Int,
    val title: Text,
    val body: Text,
    val isRefreshButtonVisible: Boolean = true,
    val refreshButtonText: Text = Text.Resource(R.string.refresh),
) {
    companion object {
        val NETWORK: ErrorState
            get() = ErrorState(
                iconResId = R.drawable.ic_wifi_error_24,
                title = Text.Resource(R.string.internet_connection_error),
                body = Text.Resource(R.string.check_internet_connection_and_refresh_page),
                isRefreshButtonVisible = true,
                refreshButtonText = Text.Resource(R.string.refresh),
            )

        val GENERIC: ErrorState
            get() = ErrorState(
                iconResId = R.drawable.ic_heart_broken_outline_24,
                title = Text.Resource(R.string.something_went_wrong),
                body = Text.Resource(R.string.refresh_page_or_come_back_later),
                isRefreshButtonVisible = true,
                refreshButtonText = Text.Resource(R.string.refresh),
            )
    }
}

@Composable
fun rememberErrorState(
    @DrawableRes
    iconResId: Int,
    title: String,
    body: String,
    isRefreshButtonVisible: Boolean = true,
    refreshButtonText: String = stringResource(R.string.refresh),
): ErrorState {
    return remember(iconResId, title, body, isRefreshButtonVisible, refreshButtonText) {
        ErrorState(
            iconResId = iconResId,
            title = Text.String(title),
            body = Text.String(body),
            isRefreshButtonVisible = isRefreshButtonVisible,
            refreshButtonText = Text.String(refreshButtonText),
        )
    }
}

fun ErrorState.Companion.from(throwable: Throwable): ErrorState = when (throwable) {
    is IOException -> NETWORK
    else -> GENERIC
}
