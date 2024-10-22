package ru.livetyping.zarina.core.uikit.error

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.text.Text

@Immutable
public data class ZarinaErrorScreenButtonState(
    val isButtonVisible: Boolean = true,
    val buttonText: Text = REFRESH_TEXT,
) {
    internal companion object {
        val REFRESH_TEXT: Text
            get() = Text.Resource(REFRESH_TEXT_RES_ID)

        val REFRESH_TEXT_RES_ID: Int
            get() = R.string.refresh
    }
}

@Composable
public fun rememberZarinaErrorButtonState(
    isButtonVisible: Boolean = true,
    buttonText: String = stringResource(ZarinaErrorScreenButtonState.REFRESH_TEXT_RES_ID),
): ZarinaErrorScreenButtonState {
    return remember(isButtonVisible, buttonText) {
        ZarinaErrorScreenButtonState(
            isButtonVisible = isButtonVisible,
            buttonText = Text.String(buttonText),
        )
    }
}
