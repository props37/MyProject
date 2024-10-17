package ru.livetyping.zarina.feature.home.ui.impl.impl.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.feature.home.domain.model.Banner
import ru.livetyping.zarina.feature.home.domain.model.SingleBanner

@Composable
internal fun FullscreenBanner(
    singleBanner: SingleBanner,
    onBannerClicked: (Banner) -> Unit,
    isOnScreen: Boolean,
    onBannerDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val banner = singleBanner.banner
    when (banner.media.type) {
        MediaType.IMAGE -> {
            ImageBanner(
                banner = banner,
                onBannerClicked = onBannerClicked,
                showTitle = false,
                onBannerDisplayed = onBannerDisplayed,
                modifier = modifier,
            )
        }

        MediaType.VIDEO -> {
            VideoBanner(
                banner = banner,
                onBannerClicked = onBannerClicked,
                isOnScreen = isOnScreen,
                onBannerDisplayed = onBannerDisplayed,
                modifier = modifier,
            )
        }
    }
}
