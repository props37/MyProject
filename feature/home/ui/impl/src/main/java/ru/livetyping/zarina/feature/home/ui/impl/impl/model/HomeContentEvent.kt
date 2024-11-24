package ru.livetyping.zarina.feature.home.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.feature.home.domain.model.Banner

@Stable
internal sealed interface HomeContentEvent {
    @Immutable
    data class BannerClicked(val banner: Banner) : HomeContentEvent

    data object RefreshTriggered : HomeContentEvent

    data object ErrorRefreshClicked : HomeContentEvent
}
