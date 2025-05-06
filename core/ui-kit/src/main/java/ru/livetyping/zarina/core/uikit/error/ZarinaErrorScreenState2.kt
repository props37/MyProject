package ru.livetyping.zarina.core.uikit.error

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.text.Text
import java.io.IOException

// TODO: [Top] Rename after full migration

@Immutable
public data class ZarinaErrorScreenState2(
    val title: Text,
    val body: Text,
    val isButtonVisible: Boolean,
    val buttonText: Text,
) {
    public companion object {
        public val NETWORK: ZarinaErrorScreenState2
            get() = ZarinaErrorScreenState2(
                title = Text.Resource(R.string.res_something_went_wrong),
                body = Text.Resource(R.string.res_internet_connection_error_guide),
                isButtonVisible = true,
                buttonText = Text.Resource(R.string.res_refresh),
            )

        public val GENERIC: ZarinaErrorScreenState2
            get() = ZarinaErrorScreenState2(
                title = Text.Resource(R.string.res_something_went_wrong),
                body = Text.Resource(R.string.res_generic_error_guide),
                isButtonVisible = true,
                buttonText = Text.Resource(R.string.res_refresh),
            )

        public fun from(throwable: Throwable): ZarinaErrorScreenState2 {
            return when (throwable) {
                is IOException -> NETWORK
                else -> GENERIC
            }
        }
    }
}
