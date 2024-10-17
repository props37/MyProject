package ru.livetyping.zarina.feature.home.ui.impl.impl.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.feature.home.domain.model.Banner

@Composable
internal fun VideoBanner(
    banner: Banner,
    onBannerClicked: (Banner) -> Unit,
    isOnScreen: Boolean,
    onBannerDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: [Top] Implement
}
