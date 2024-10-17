package ru.livetyping.zarina.feature.home.ui.impl.impl.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.mediacompose.VideoPlayer
import ru.livetyping.zarina.feature.home.domain.model.Banner

@Composable
internal fun VideoBanner(
    banner: Banner,
    onBannerClicked: (Banner) -> Unit,
    isOnScreen: Boolean,
    onBannerDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VideoPlayer(
        url = banner.media.originalUrl.value,
        isOnScreen = isOnScreen,
        onReadyToPlay = onBannerDisplayed,
        modifier = modifier
            .clickable(
                enabled = banner.clickAction != null,
                onClick = { onBannerClicked(banner) },
            ),
    )
}
