package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component

import androidx.compose.runtime.Immutable

@Immutable
internal data class TopBarState(
    val isClearButtonVisible: Boolean,
    val isClearButtonLoading: Boolean,
) {
    companion object {
        fun getInitial(): TopBarState {
            return TopBarState(
                isClearButtonVisible = false,
                isClearButtonLoading = false,
            )
        }
    }
}
