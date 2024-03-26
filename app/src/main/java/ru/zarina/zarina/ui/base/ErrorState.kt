package ru.zarina.zarina.ui.base

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.base.text.Text
import java.io.IOException

data class ErrorState(
    @DrawableRes
    val iconResId: Int,
    val title: Text,
    val body: Text,
    val isButtonVisible: Boolean = true,
    val buttonText: Text = ButtonText,
) {
    companion object {
        val NETWORK: ErrorState
            get() = ErrorState(
                iconResId = R.drawable.ic_wifi_error_64,
                title = Text.Resource(R.string.internet_connection_error),
                body = Text.Resource(R.string.check_internet_connection_and_refresh_page),
                isButtonVisible = true,
                buttonText = ButtonText,
            )

        val GENERIC: ErrorState
            get() = ErrorState(
                iconResId = R.drawable.ic_heart_broken_outline_64,
                title = Text.Resource(R.string.something_went_wrong),
                body = Text.Resource(R.string.refresh_page_or_come_back_later),
                isButtonVisible = true,
                buttonText = ButtonText,
            )

        private val ButtonText: Text get() = Text.Resource(R.string.refresh)
    }
}

@Composable
fun rememberErrorState(
    @DrawableRes
    iconResId: Int,
    title: String,
    body: String,
    isButtonVisible: Boolean = true,
    buttonText: String = stringResource(R.string.refresh),
): ErrorState {
    return remember(iconResId, title, body, isButtonVisible, buttonText) {
        ErrorState(
            iconResId = iconResId,
            title = Text.String(title),
            body = Text.String(body),
            isButtonVisible = isButtonVisible,
            buttonText = Text.String(buttonText),
        )
    }
}

fun ErrorState.Companion.from(throwable: Throwable): ErrorState = when (throwable) {
    is IOException -> NETWORK
    else -> GENERIC
}
