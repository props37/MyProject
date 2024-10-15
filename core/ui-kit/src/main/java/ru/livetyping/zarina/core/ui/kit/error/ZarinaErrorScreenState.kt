package ru.livetyping.zarina.core.ui.kit.error

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.ui.kit.R
import java.io.IOException

@Immutable
public data class ZarinaErrorScreenState(
    @DrawableRes
    val iconResId: Int,
    val title: Text,
    val body: Text,
    val buttonState: ZarinaErrorScreenButtonState,
) {
    public companion object {
        public val NETWORK: ZarinaErrorScreenState
            get() = ZarinaErrorScreenState(
                iconResId = R.drawable.ic_wifi_error_64,
                title = Text.Resource(R.string.internet_connection_error),
                body = Text.Resource(R.string.check_internet_connection_and_refresh_page),
                buttonState = ZarinaErrorScreenButtonState(),
            )

        public val GENERIC: ZarinaErrorScreenState
            get() = ZarinaErrorScreenState(
                iconResId = R.drawable.ic_heart_broken_outline_64,
                title = Text.Resource(R.string.something_went_wrong),
                body = Text.Resource(R.string.refresh_page_or_come_back_later),
                buttonState = ZarinaErrorScreenButtonState(),
            )

        public fun from(throwable: Throwable): ZarinaErrorScreenState {
            return when (throwable) {
                is IOException -> NETWORK
                else -> GENERIC
            }
        }
    }
}

@Composable
public fun rememberZarinaErrorScreenState(
    @DrawableRes
    iconResId: Int,
    title: String,
    body: String,
    buttonState: ZarinaErrorScreenButtonState = rememberZarinaErrorButtonState(),
): ZarinaErrorScreenState {
    return remember(iconResId, title, body, buttonState) {
        ZarinaErrorScreenState(
            iconResId = iconResId,
            title = Text.String(title),
            body = Text.String(body),
            buttonState = buttonState,
        )
    }
}
