package ru.zarina.zarina.ui.screen.home

import ru.zarina.zarina.domain.rework.content.HomeContent

sealed class HomeScreenAction {
    data class BannerClicked(val banner: HomeContent.Banner) : HomeScreenAction()
}
