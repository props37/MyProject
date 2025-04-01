package ru.livetyping.zarina.presentation.screen.home.tooling.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.MediaType
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.content.HomeContent
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.screen.home.HomeViewModel
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
            videoPlaceholder = null,
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
