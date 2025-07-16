package ru.livetyping.zarina.feature.home.ui.impl.screen

import ru.livetyping.zarina.feature.home.domain.model.Banner

internal sealed class HomeScreenAction {
    data class BannerClicked(val banner: Banner) : HomeScreenAction()
}
