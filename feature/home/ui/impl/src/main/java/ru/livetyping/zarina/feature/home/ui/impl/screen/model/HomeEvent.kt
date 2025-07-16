package ru.livetyping.zarina.feature.home.ui.impl.screen.model

import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.feature.home.domain.model.Banner

internal sealed interface HomeEvent {
    data class GenderSelected(val tab: GenderTab) : HomeEvent

    data class BannerClicked(val banner: Banner) : HomeEvent

    data object PullRefreshTriggered : HomeEvent

    data object RefreshClicked : HomeEvent
}
