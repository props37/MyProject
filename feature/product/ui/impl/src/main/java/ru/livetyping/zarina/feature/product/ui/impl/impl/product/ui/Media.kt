package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.core.mediacompose.SimpleVideoPlayer
import ru.livetyping.zarina.core.uikit.product.ProductDefaults
import ru.livetyping.zarina.core.uikit.shimmer.shimmerToggleable
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
internal fun Media(
    media: Media,
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
) {
    var isMediaDisplayed by remember(media) { mutableStateOf(false) }

    Box(
        modifier = modifier
            .aspectRatio(ProductDefaults.MediaAspectRatio)
            .shimmerToggleable(
                shimmer = shimmer,
                isEnabled = !isMediaDisplayed,
            )
            .background(UiKitTheme2.colors.skeletonBackground),
    ) {
        when (media.type) {
            MediaType.IMAGE -> {
                AsyncImage(
                    model = media.originalUrl.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onSuccess = { isMediaDisplayed = true },
                    modifier = Modifier.matchParentSize(),
                )
            }

            MediaType.VIDEO -> {
                SimpleVideoPlayer(
                    url = media.originalUrl.value,
                    contentScale = ContentScale.Crop,
                    onReadyToPlay = { isMediaDisplayed = true },
                    modifier = Modifier.matchParentSize(),
                )
            }
        }
    }
}
