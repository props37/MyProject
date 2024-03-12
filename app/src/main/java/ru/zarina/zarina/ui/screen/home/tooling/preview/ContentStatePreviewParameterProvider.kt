package ru.zarina.zarina.ui.screen.home.tooling.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.zarina.zarina.domain.common.Media
import ru.zarina.zarina.domain.common.MediaType
import ru.zarina.zarina.domain.common.Url
import ru.zarina.zarina.domain.content.HomeContent
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.screen.home.HomeViewModel
import kotlin.random.Random

class ContentStatePreviewParameterProvider : PreviewParameterProvider<HomeViewModel.ContentState> {
    override val values: Sequence<HomeViewModel.ContentState>
        get() = sequenceOf(
            HomeViewModel.ContentState.Success(getHomeContent()),
            HomeViewModel.ContentState.Loading,
            HomeViewModel.ContentState.Error(ErrorState.NETWORK),
        )

    private fun getHomeContent(): HomeContent {
        val banner = HomeContent.Banner(
            id = HomeContent.Banner.Id(Random.nextLong()),
            media = Media(
                url = Url(""),
                type = MediaType.IMAGE,
            ),
            title = "Заголовок",
            clickAction = null,
        )
        val womenBanners = listOf(
            HomeContent.BannerContainer.SingleBanner(banner),
            HomeContent.BannerContainer.MultipleBanners(
                banners = listOf(banner, banner, banner, banner),
                arrangement = HomeContent.BannerContainer.MultipleBanners.Arrangement.GRID,
            ),
            HomeContent.BannerContainer.SingleBanner(banner),
        )
        return HomeContent(
            womenBanners = womenBanners,
            menBanners = womenBanners,
        )
    }
}
