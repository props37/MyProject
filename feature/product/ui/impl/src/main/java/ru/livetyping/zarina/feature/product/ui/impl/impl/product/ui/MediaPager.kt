package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.valentinilk.shimmer.shimmer
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.core.uicompose.pager.EndlessPagerStateUtils
import ru.livetyping.zarina.core.uicompose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.core.uikit.product.ProductDefaults
import ru.livetyping.zarina.core.uikit.shimmer.shimmerToggleable
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import timber.log.Timber

@Composable
internal fun MediaPager(
    mediaList: List<Media>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberEndlessPagerState(itemCount = mediaList.size)
    val shimmer = rememberZarinaSkeletonShimmer()

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        val media = EndlessPagerStateUtils.getLooping(mediaList, page)
        var isMediaDisplayed by remember(media) { mutableStateOf(false) }

        when (media?.type) {
            MediaType.IMAGE -> {
                AsyncImage(
                    model = media?.originalUrl?.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onSuccess = { isMediaDisplayed = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ProductDefaults.MediaAspectRatio)
                        .shimmerToggleable(
                            shimmer = shimmer,
                            isEnabled = !isMediaDisplayed,
                        )
                        .background(UiKitTheme2.colors.skeletonBackground),
                )
            }

            MediaType.VIDEO -> {
                // TODO: [Top] Implement
                SideEffect {
                    Timber.tag(Tag).w("Video are not supported")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ProductDefaults.MediaAspectRatio)
                        .shimmer(shimmer)
                        .background(UiKitTheme2.colors.skeletonBackground),
                )
            }

            else -> Unit
        }
    }
}

private const val Tag = "MediaPager"
