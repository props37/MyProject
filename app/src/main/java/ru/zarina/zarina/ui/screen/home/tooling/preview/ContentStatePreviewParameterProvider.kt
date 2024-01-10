package ru.zarina.zarina.ui.screen.home.tooling.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.content.HomeContent
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.screen.home.HomeViewModel
import kotlin.random.Random

class ContentStatePreviewParameterProvider : PreviewParameterProvider<HomeViewModel.ContentState> {
    override val values: Sequence<HomeViewModel.ContentState>
        get() = sequenceOf(
            HomeViewModel.ContentState.Success(getHomeContent()),
            HomeViewModel.ContentState.Loading,
            HomeViewModel.ContentState.Error(ErrorStateRework.NETWORK),
        )

    private fun getHomeContent(): HomeContent {
        val bannerItem = HomeContent.Banner.Item(
            id = HomeContent.Banner.Item.Id(Random.nextLong()),
            mediaType = MediaType.IMAGE,
            mediaUrl = Url(""),
            title = "Заголовок",
        )
        val womenBanners = listOf(
            HomeContent.Banner.SingleItem(bannerItem),
            HomeContent.Banner.MultipleItems(
                items = listOf(bannerItem, bannerItem, bannerItem, bannerItem),
                viewType = HomeContent.Banner.MultipleItems.ViewType.GRID,
            ),
            HomeContent.Banner.SingleItem(bannerItem),
        )
        return HomeContent(
            womenBanners = womenBanners,
            menBanners = womenBanners,
        )
    }
}
