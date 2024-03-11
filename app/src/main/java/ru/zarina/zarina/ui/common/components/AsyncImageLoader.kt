package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun AsyncImageLoader(
    url: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
) {
    var isLoaded by remember(url) { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        label = "content alpha"
    )
    Box(modifier = modifier) {
        AsyncImage(
            model = url,
            onSuccess = { isLoaded = true },
            alignment = alignment,
            contentScale = contentScale,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = contentAlpha },
        )
        if (!isLoaded)
            CircularProgressIndicator(
                color = UiKitTheme.colors.primaryContentColor,
                modifier = Modifier.align(Alignment.Center)
            )
    }
}
