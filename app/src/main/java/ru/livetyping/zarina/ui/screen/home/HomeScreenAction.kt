package ru.livetyping.zarina.ui.screen.home

import ru.livetyping.zarina.domain.content.HomeContent

sealed class HomeScreenAction {
    data class BannerClicked(val banner: HomeContent.Banner) : HomeScreenAction()
}
