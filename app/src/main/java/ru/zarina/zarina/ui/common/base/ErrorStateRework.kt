package ru.zarina.zarina.ui.common.base

import androidx.annotation.DrawableRes
import ru.zarina.zarina.R

data class ErrorStateRework(
    @DrawableRes
    val iconResId: Int,
    val title: Text,
    val body: Text,
    val isRefreshButtonVisible: Boolean = true,
    val refreshButtonText: Text = Text.Resource(R.string.refresh),
) {
    companion object {
        val NETWORK: ErrorStateRework
            get() = ErrorStateRework(
                iconResId = R.drawable.ic_wifi_error_24,
                title = Text.Resource(R.string.internet_connection_error),
                body = Text.Resource(R.string.check_internet_connection_and_refresh_page),
                isRefreshButtonVisible = true,
                refreshButtonText = Text.Resource(R.string.refresh),
            )

        val GENERIC: ErrorStateRework
            get() = ErrorStateRework(
                iconResId = R.drawable.ic_heart_broken_outline_24,
                title = Text.Resource(R.string.something_went_wrong),
                body = Text.Resource(R.string.refresh_page_or_come_back_later),
                isRefreshButtonVisible = true,
                refreshButtonText = Text.Resource(R.string.refresh),
            )
    }
}
