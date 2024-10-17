package ru.livetyping.zarina.feature.home.ui.impl.impl.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.feature.home.domain.model.Banner
import ru.livetyping.zarina.feature.home.domain.model.MultipleBanners
import timber.log.Timber

@Composable
internal fun GridBanners(
    multipleBanners: MultipleBanners,
    onBannerClicked: (Banner) -> Unit,
    onBannerDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val bannerRows = remember(multipleBanners.banners) {
            List(BannerItemCount) { index -> multipleBanners.banners.getOrNull(index) }
                .chunked(BannerRowItemCount)
        }

        for (i in bannerRows.indices) {
            Row(modifier = Modifier.weight(1f)) {
                val rowBanners = bannerRows[i]

                val bannerModifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)

                for (j in rowBanners.indices) {
                    val banner = rowBanners[j]
                    when (banner?.media?.type) {
                        MediaType.IMAGE -> {
                            ImageBanner(
                                banner = banner,
                                onBannerClicked = onBannerClicked,
                                showTitle = true,
                                onBannerDisplayed = onBannerDisplayed,
                                modifier = bannerModifier,
                            )
                        }

                        MediaType.VIDEO -> {
                            SideEffect {
                                Timber.w("Video banners are not supported in Grid view")
                            }
                            Box(modifier = bannerModifier)
                        }

                        null -> {
                            Box(modifier = bannerModifier)
                        }
                    }
                }
            }
        }
    }
}

private const val BannerItemCount = 4
private const val BannerRowItemCount = BannerItemCount / 2
