package ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.ui.kit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

@Stable
internal sealed class HomeContentState {
    data object Loading : HomeContentState()

    @Immutable
    data class Success(val content: HomeContent) : HomeContentState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : HomeContentState()
}
