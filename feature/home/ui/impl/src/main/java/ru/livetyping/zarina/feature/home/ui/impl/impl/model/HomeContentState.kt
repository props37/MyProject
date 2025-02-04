package ru.livetyping.zarina.feature.home.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

@Stable
internal sealed class HomeContentState {
    abstract val isRefreshing: Boolean

    @Immutable
    data class Success(
        val content: HomeContent,
        override val isRefreshing: Boolean,
    ) : HomeContentState()

    data object Loading : HomeContentState() {
        override val isRefreshing = false
    }

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : HomeContentState() {
        override val isRefreshing = false
    }
}
