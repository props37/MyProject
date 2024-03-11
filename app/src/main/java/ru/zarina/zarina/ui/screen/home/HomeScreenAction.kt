package ru.zarina.zarina.ui.screen.home

import ru.zarina.zarina.domain.content.HomeContent

sealed class HomeScreenAction {
    data class BannerClicked(val banner: HomeContent.Banner) : HomeScreenAction()
}
