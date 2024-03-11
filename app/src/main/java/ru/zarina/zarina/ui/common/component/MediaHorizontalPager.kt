package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import ru.zarina.zarina.domain.common.Media
import ru.zarina.zarina.domain.common.MediaType
import ru.zarina.zarina.ui.common.component.media.VideoPlayer
import ru.zarina.zarina.ui.common.component.skeleton.Skeleton
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
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
        flingBehavior = PagerDefaults.flingBehavior(
            state = pagerState,
            snapAnimationSpec = spring(stiffness = Spring.StiffnessMedium),
        ),
        modifier = modifier,
    ) { page ->
        val media = medias.loopingGet(page)
        var isMediaDisplayed by remember(media) { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            when (media?.type) {
                MediaType.IMAGE -> {
                    AsyncImage(
                        model = media.url.value,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        onSuccess = { isMediaDisplayed = true },
                        modifier = Modifier.matchParentSize(),
                    )
                }

                MediaType.VIDEO -> {
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
                modifier = Modifier.matchParentSize(),
            ) {
                Skeleton(
                    shimmer = shimmer,
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UiKitTheme.colorsReworked.background.general.regular.default),
                )
            }
        }
    }
}
