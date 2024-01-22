package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import ru.zarina.zarina.domain.rework.common.Media
import ru.zarina.zarina.ui.common.component.base.media.VideoPlayer
import ru.zarina.zarina.ui.common.component.base.skeleton.Skeleton
import ru.zarina.zarina.ui.common.component.base.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.kotlin.loopingGet

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaHorizontalPager(
    pagerState: PagerState,
    medias: List<Media>,
    modifier: Modifier = Modifier,
    shimmer: Shimmer? = rememberSkeletonShimmer(),
) {
    val placeholderEnterTransition = remember { fadeIn() }
    val placeholderExitTransition = remember { fadeOut() }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        val media = medias.loopingGet(page)
        var isMediaDisplayed by remember(media) { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            when (media?.type) {
                Media.Type.IMAGE -> {
                    AsyncImage(
                        model = media.url.value,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        onSuccess = { isMediaDisplayed = true },
                        modifier = Modifier.matchParentSize(),
                    )
                }

                Media.Type.VIDEO -> {
                    VideoPlayer(
                        url = media.url,
                        onReadyToPlay = { isMediaDisplayed = true },
                        modifier = Modifier.matchParentSize(),
                    )
                }

                null -> Unit
            }

            AnimatedVisibility(
                visible = !isMediaDisplayed,
                enter = placeholderEnterTransition,
                exit = placeholderExitTransition,
                modifier = Modifier.fillMaxSize(),
            ) {
                Skeleton(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UiKitTheme.colorsReworked.background.general.regular.default),
                )
            }
        }
    }
}
