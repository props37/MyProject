package ru.livetyping.zarina.core.uikit.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.core.mediacompose.VideoPlayer
import ru.livetyping.zarina.core.uikit.impl.util.loopingGet
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaMediaHorizontalPager(
    pagerState: PagerState,
    media: List<Media>,
    modifier: Modifier = Modifier,
    quality: ZarinaMediaHorizontalPagerQuality = ZarinaMediaHorizontalPagerQuality.Original,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
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
        @Suppress("NAME_SHADOWING")
        val media = media.loopingGet(page)
        var isMediaDisplayed by remember(media) { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            when (media?.type) {
                MediaType.IMAGE -> {
                    val url = when (quality) {
                        ZarinaMediaHorizontalPagerQuality.Original -> media.originalUrl
                        ZarinaMediaHorizontalPagerQuality.Thumbnail -> media.thumbnailUrl
                    }
                    AsyncImage(
                        model = url.value,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        onSuccess = { isMediaDisplayed = true },
                        modifier = Modifier.matchParentSize(),
                    )
                }

                MediaType.VIDEO -> {
                    VideoPlayer(
                        url = media.originalUrl.value,
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
                ZarinaSkeleton(
                    shimmer = shimmer,
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UiKitTheme.colors.background.general.regular.default),
                )
            }
        }
    }
}

public enum class ZarinaMediaHorizontalPagerQuality { Original, Thumbnail }
