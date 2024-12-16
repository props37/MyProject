package ru.livetyping.zarina.feature.home.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

@Stable
internal sealed class HomeContentState(
    open val isRefreshing: Boolean,
) {
    @Immutable
    data class Success(
        val content: HomeContent,
        override val isRefreshing: Boolean,
    ) : HomeContentState(isRefreshing = isRefreshing)

    data object Loading : HomeContentState(isRefreshing = false)

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : HomeContentState(isRefreshing = false)
}
