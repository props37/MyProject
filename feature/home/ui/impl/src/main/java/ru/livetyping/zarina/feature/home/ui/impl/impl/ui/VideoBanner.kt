package ru.livetyping.zarina.feature.home.ui.impl.impl.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import ru.livetyping.zarina.core.mediacompose.SimpleVideoPlayer
import ru.livetyping.zarina.feature.home.domain.model.Banner

@OptIn(UnstableApi::class)
@Composable
internal fun VideoBanner(
    banner: Banner,
    onBannerClicked: (Banner) -> Unit,
    isOnScreen: Boolean,
    onBannerDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clickable(
                enabled = banner.clickAction != null,
                onClick = { onBannerClicked(banner) },
            )
    ) {
        var isVideoPlaceholderVisible by remember(banner) { mutableStateOf(true) }

        SimpleVideoPlayer(
            url = banner.media.originalUrl.value,
            isOnScreen = isOnScreen,
            contentScale = ContentScale.Crop,
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING,
            onReadyToPlay = {
                isVideoPlaceholderVisible = false
                onBannerDisplayed()
            },
            modifier = Modifier.matchParentSize(),
        )

        val videoPlaceholderUrl = banner.videoPlaceholderUrl
        if (isVideoPlaceholderVisible && videoPlaceholderUrl != null) {
            AsyncImage(
                model = videoPlaceholderUrl.value,
                contentDescription = banner.title,
                contentScale = ContentScale.Crop,
                onSuccess = { onBannerDisplayed() },
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}
