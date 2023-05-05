package ru.zarina.zarina.ui.common.base

import androidx.annotation.DrawableRes
import ru.zarina.zarina.R

data class ErrorState(
    @DrawableRes
    val icon: Int? = null,
    val title: Text? = null,
    val subtitle: Text? = null,
    val isButtonVisible: Boolean = false,
    val buttonText: Text? = null,
) {

    companion object {

        val GENERIC
            get() = ErrorState(
                icon = R.drawable.ic_broken_heart_96,
                title = Text.Resource(R.string.something_went_wrong),
                subtitle = Text.Resource(R.string.try_again_later),
                isButtonVisible = true,
                buttonText = Text.Resource(R.string.refresh),
            )

        val NETWORK
            get() = ErrorState(
                icon = R.drawable.ic_no_network_96,
                title = Text.Resource(R.string.loading_error),
                subtitle = Text.Resource(R.string.check_connection_and_try_again_later),
                isButtonVisible = true,
                buttonText = Text.Resource(R.string.refresh),
            )

    }

}
