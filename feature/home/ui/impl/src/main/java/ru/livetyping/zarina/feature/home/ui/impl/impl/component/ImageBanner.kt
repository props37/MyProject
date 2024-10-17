package ru.livetyping.zarina.feature.home.ui.impl.impl.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.home.domain.model.Banner

@Composable
internal fun ImageBanner(
    banner: Banner,
    onBannerClicked: (Banner) -> Unit,
    showTitle: Boolean,
    onBannerDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clickable(
                enabled = banner.clickAction != null,
                onClick = { onBannerClicked(banner) },
            ),
    ) {
        AsyncImage(
            model = banner.media.originalUrl.value,
            contentDescription = banner.title,
            contentScale = ContentScale.Crop,
            onSuccess = { onBannerDisplayed() },
            modifier = Modifier.matchParentSize(),
        )

        if (showTitle) {
            Text(
                text = banner.title?.uppercase().orEmpty(),
                style = UiKitTheme.typography.tertiary.regular,
                color = UiKitTheme.colors.text.general.inversed.default,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .padding(horizontal = 12.dp),
            )
        }
    }
}
